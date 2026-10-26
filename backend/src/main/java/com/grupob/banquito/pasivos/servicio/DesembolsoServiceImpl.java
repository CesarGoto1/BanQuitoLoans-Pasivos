package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.SolicitudDesembolso;
import com.grupob.banquito.pasivos.dto.RespuestaDesembolso;
import com.grupob.banquito.pasivos.dto.DesembolsoResultado;
import com.grupob.banquito.pasivos.entidad.Cuenta;
import com.grupob.banquito.pasivos.entidad.EstadoCliente;
import com.grupob.banquito.pasivos.entidad.EstadoCuenta;
import com.grupob.banquito.pasivos.entidad.Movimiento;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import com.grupob.banquito.pasivos.repositorio.MovimientoRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DesembolsoServiceImpl implements DesembolsoService {

    private static final String VERSION = "1.0";
    private static final String CONCEPTO = "DESEMBOLSO";
    private static final String TIPO = "CREDITO";
    private static final String USUARIO_AUDITORIA = "core-activos";

    private final CuentaRepository cuentaRepositorio;
    private final MovimientoRepository movimientoRepositorio;

    public DesembolsoServiceImpl(CuentaRepository cuentaRepositorio, MovimientoRepository movimientoRepositorio) {
        this.cuentaRepositorio = cuentaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
    }

    @Override
    @Transactional
    public DesembolsoResultado acreditar(SolicitudDesembolso solicitud) {
        if (!VERSION.equals(solicitud.version())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Versión de instrucción no soportada: " + solicitud.version() + ".");
        }
        if (solicitud.montoAprobado().subtract(solicitud.contribucionSolca()).compareTo(solicitud.montoNeto()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El monto neto debe ser el monto aprobado menos la contribución SOLCA.");
        }

        Cuenta cuenta = cuentaRepositorio.findParaActualizarByNumeroCuenta(solicitud.cuentaDestino())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));

        Movimiento existente = movimientoRepositorio
                .findFirstByNumeroOperacionAndConceptoMovimiento(solicitud.numeroPrestamo(), CONCEPTO).orElse(null);
        if (existente != null) {
            if (!mismaInstruccion(existente, solicitud)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "La operación " + solicitud.numeroPrestamo() + " ya fue acreditada con otros datos.");
            }
            return new DesembolsoResultado(RespuestaDesembolso.desde(existente), false);
        }

        if (!cuenta.getCliente().getCedulaCliente().equals(solicitud.cedulaCliente())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La cuenta no pertenece al cliente indicado.");
        }
        if (cuenta.getEstadoCuenta() != EstadoCuenta.ACTIVA || cuenta.getCliente().getEstadoCliente() != EstadoCliente.ACTIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La cuenta o el cliente no están activos.");
        }

        LocalDateTime ahora = LocalDateTime.now();
        cuenta.setSaldoCuenta(cuenta.getSaldoCuenta().add(solicitud.montoNeto()));
        cuenta.setUsuarioModificacion(USUARIO_AUDITORIA);
        cuenta.setFechaModificacion(ahora);

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setTipoMovimiento(TIPO);
        movimiento.setConceptoMovimiento(CONCEPTO);
        movimiento.setMontoMovimiento(solicitud.montoNeto());
        movimiento.setSaldoPosterior(cuenta.getSaldoCuenta());
        movimiento.setFechaMovimiento(solicitud.fechaDesembolso());
        movimiento.setFechaHoraMovimiento(ahora);
        movimiento.setUuidTransaccion(UUID.randomUUID());
        movimiento.setNumeroOperacion(solicitud.numeroPrestamo());
        movimiento.setReferencia(solicitud.referencia());
        movimiento.setUsuarioCreacion(USUARIO_AUDITORIA);
        movimiento.setFechaCreacion(ahora);
        movimientoRepositorio.save(movimiento);

        return new DesembolsoResultado(RespuestaDesembolso.desde(movimiento), true);
    }

    private boolean mismaInstruccion(Movimiento existente, SolicitudDesembolso solicitud) {
        return existente.getCuenta().getNumeroCuenta().equals(solicitud.cuentaDestino())
                && existente.getMontoMovimiento().compareTo(solicitud.montoNeto()) == 0;
    }
}
