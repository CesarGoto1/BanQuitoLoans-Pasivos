package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaDebito;
import com.grupob.banquito.pasivos.dto.SolicitudDebito;
import com.grupob.banquito.pasivos.entidad.Cuenta;
import com.grupob.banquito.pasivos.entidad.EstadoCliente;
import com.grupob.banquito.pasivos.entidad.EstadoCuenta;
import com.grupob.banquito.pasivos.entidad.Movimiento;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import com.grupob.banquito.pasivos.repositorio.MovimientoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DebitoServiceImpl implements DebitoService {

    private static final String CONCEPTO = "COBRO_CUOTA";
    private static final String TIPO = "DEBITO";
    private static final String CANAL = "DEBITO_AUTOMATICO";
    private static final String USUARIO = "core-pasivos";
    private static final int REINTENTO_MINUTOS = 60;
    private static final int ESCALA_MONETARIA = 2;
    private static final BigDecimal CERO = BigDecimal.ZERO.setScale(ESCALA_MONETARIA);

    private final CuentaRepository cuentaRepositorio;
    private final MovimientoRepository movimientoRepositorio;

    public DebitoServiceImpl(CuentaRepository cuentaRepositorio, MovimientoRepository movimientoRepositorio) {
        this.cuentaRepositorio = cuentaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
    }

    @Override
    @Transactional
    public RespuestaDebito solicitar(SolicitudDebito solicitud) {
        BigDecimal solicitado = dinero(solicitud.montoSolicitado());
        List<Cuenta> cuentasActivas = cuentaRepositorio
                .findByClienteCedulaClienteAndEstadoCuentaOrderByNumeroCuenta(
                        solicitud.identificacionCliente(), EstadoCuenta.ACTIVA);

        if (cuentasActivas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existen cuentas activas para el cliente indicado.");
        }
        if (cuentasActivas.stream().anyMatch(cuenta ->
                cuenta.getCliente().getEstadoCliente() != EstadoCliente.ACTIVO)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "El cliente no está activo.");
        }

        Cuenta principal = cuentasActivas.stream()
                .filter(cuenta -> cuenta.getNumeroCuenta().equals(solicitud.cuentaPrincipal()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                        "La cuenta principal no pertenece al cliente o no está activa."));

        List<Cuenta> cuentasParaCobro = new ArrayList<>();
        cuentasParaCobro.add(principal);
        if (solicitud.permitirBarridoMulticuenta()) {
            cuentasParaCobro.addAll(cuentasActivas.stream()
                    .filter(cuenta -> cuenta != principal)
                    .toList());
        }

        BigDecimal disponible = cuentasParaCobro.stream()
                .map(Cuenta::getSaldoCuenta)
                .reduce(CERO, BigDecimal::add);
        if (!solicitud.permitirDebitoParcial() && disponible.compareTo(solicitado) < 0) {
            return construirRespuesta(solicitado, solicitado, List.of());
        }

        UUID uuid = UUID.randomUUID();
        List<RespuestaDebito.CobroCuenta> cobros = new ArrayList<>();
        BigDecimal pendiente = solicitado;
        LocalDateTime ahora = LocalDateTime.now();

        for (Cuenta cuenta : cuentasParaCobro) {
            if (pendiente.signum() == 0) {
                break;
            }
            BigDecimal cobrado = debitar(cuenta, pendiente, solicitud, ahora);
            if (cobrado.signum() > 0) {
                cobros.add(new RespuestaDebito.CobroCuenta(
                        cuenta.getNumeroCuenta(), cobrado,
                        cuenta == principal ? "PRINCIPAL" : "BARRIDO_SECUNDARIA"));
                pendiente = pendiente.subtract(cobrado).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
            }
        }

        return construirRespuesta(solicitado, pendiente, cobros, uuid, ahora);
    }

    private BigDecimal debitar(Cuenta cuenta, BigDecimal pendiente, SolicitudDebito solicitud,
                               LocalDateTime ahora) {
        BigDecimal saldo = dinero(cuenta.getSaldoCuenta());
        BigDecimal cobrado = saldo.min(pendiente).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        if (cobrado.signum() == 0) {
            return CERO;
        }

        cuenta.setSaldoCuenta(saldo.subtract(cobrado).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP));
        cuenta.setUsuarioModificacion(USUARIO);
        cuenta.setFechaModificacion(ahora);

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setTipoMovimiento(TIPO);
        movimiento.setConceptoMovimiento(CONCEPTO);
        movimiento.setMontoMovimiento(cobrado);
        movimiento.setSaldoPosterior(cuenta.getSaldoCuenta());
        movimiento.setFechaMovimiento(LocalDate.now());
        movimiento.setFechaHoraMovimiento(ahora);
        movimiento.setUuidTransaccion(UUID.randomUUID());
        movimiento.setNumeroOperacion(solicitud.numeroOperacion());
        movimiento.setCanal(CANAL);
        movimiento.setReferencia("Cobro automático de cuota " + solicitud.numeroOperacion());
        movimiento.setUsuarioCreacion(USUARIO);
        movimiento.setFechaCreacion(ahora);
        movimientoRepositorio.save(movimiento);
        return cobrado;
    }

    private RespuestaDebito construirRespuesta(BigDecimal solicitado, BigDecimal pendiente,
                                               List<RespuestaDebito.CobroCuenta> cobros) {
        return construirRespuesta(solicitado, pendiente, cobros, UUID.randomUUID(), LocalDateTime.now());
    }

    private RespuestaDebito construirRespuesta(BigDecimal solicitado, BigDecimal pendiente,
                                               List<RespuestaDebito.CobroCuenta> cobros,
                                               UUID uuid, LocalDateTime ahora) {
        BigDecimal cobrado = solicitado.subtract(pendiente).setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
        RespuestaDebito.EstadoDebito estado = pendiente.signum() == 0
                ? RespuestaDebito.EstadoDebito.PAGADA
                : cobrado.signum() == 0
                ? RespuestaDebito.EstadoDebito.SIN_FONDOS
                : RespuestaDebito.EstadoDebito.PARCIAL;
        return new RespuestaDebito(uuid, estado, solicitado, cobrado, pendiente,
                cobros, pendiente.signum() == 0 ? null : REINTENTO_MINUTOS, ahora);
    }

    private BigDecimal dinero(BigDecimal monto) {
        return monto.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }
}
