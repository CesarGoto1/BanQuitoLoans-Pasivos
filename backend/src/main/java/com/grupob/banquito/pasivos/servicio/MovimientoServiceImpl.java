package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaMovimiento;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import com.grupob.banquito.pasivos.repositorio.MovimientoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MovimientoServiceImpl implements MovimientoService {

    private final CuentaRepository cuentaRepositorio;
    private final MovimientoRepository movimientoRepositorio;

    public MovimientoServiceImpl(CuentaRepository cuentaRepositorio, MovimientoRepository movimientoRepositorio) {
        this.cuentaRepositorio = cuentaRepositorio;
        this.movimientoRepositorio = movimientoRepositorio;
    }

    @Override
    public Page<RespuestaMovimiento> buscarPorNumeroCuenta(String numeroCuenta, Pageable paginacion) {
        cuentaRepositorio.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));
        Sort orden = Sort.by(paginacion.getSort().stream()
                .map(criterio -> criterio.withProperty(traducirPropiedad(criterio.getProperty())))
                .toList());
        Pageable paginacionConsulta = paginacion.isPaged()
                ? PageRequest.of(paginacion.getPageNumber(), paginacion.getPageSize(), orden)
                : Pageable.unpaged(orden);
        Page<RespuestaMovimiento> movimientos = movimientoRepositorio
                .findByCuentaNumeroCuenta(numeroCuenta, paginacionConsulta)
                .map(RespuestaMovimiento::desde);
        return new PageImpl<>(movimientos.getContent(), paginacion, movimientos.getTotalElements());
    }

    // Los clientes de la API pueden seguir usando los nombres de ordenamiento anteriores.
    private String traducirPropiedad(String propiedad) {
        Class<?> entidad = com.grupob.banquito.pasivos.entidad.Movimiento.class;
        StringBuilder ruta = new StringBuilder();
        for (String segmento : propiedad.split("\\.")) {
            Map<String, String> equivalencias;
            if (entidad == com.grupob.banquito.pasivos.entidad.Movimiento.class) {
                equivalencias = Map.ofEntries(
                        Map.entry("id", "idMovimiento"),
                        Map.entry("type", "tipoMovimiento"),
                        Map.entry("concept", "conceptoMovimiento"),
                        Map.entry("amount", "montoMovimiento"),
                        Map.entry("balanceAfter", "saldoPosterior"),
                        Map.entry("movementDate", "fechaMovimiento"),
                        Map.entry("movementDateTime", "fechaHoraMovimiento"),
                        Map.entry("transactionUuid", "uuidTransaccion"),
                        Map.entry("operationNumber", "numeroOperacion"),
                        Map.entry("channel", "canal"),
                        Map.entry("reference", "referencia"),
                        Map.entry("createdBy", "usuarioCreacion"),
                        Map.entry("createdDate", "fechaCreacion"),
                        Map.entry("lastModifiedBy", "usuarioModificacion"),
                        Map.entry("lastModifiedDate", "fechaModificacion"),
                        Map.entry("account", "cuenta"));
            } else if (entidad == com.grupob.banquito.pasivos.entidad.Cuenta.class) {
                equivalencias = Map.ofEntries(
                        Map.entry("id", "idCuenta"),
                        Map.entry("accountNumber", "numeroCuenta"),
                        Map.entry("type", "tipoCuenta"),
                        Map.entry("balance", "saldoCuenta"),
                        Map.entry("openingDate", "fechaApertura"),
                        Map.entry("status", "estadoCuenta"),
                        Map.entry("createdBy", "usuarioCreacion"),
                        Map.entry("createdDate", "fechaCreacion"),
                        Map.entry("lastModifiedBy", "usuarioModificacion"),
                        Map.entry("lastModifiedDate", "fechaModificacion"),
                        Map.entry("customer", "cliente"));
            } else if (entidad == com.grupob.banquito.pasivos.entidad.Cliente.class) {
                equivalencias = Map.ofEntries(
                        Map.entry("id", "idCliente"),
                        Map.entry("identificationNumber", "cedulaCliente"),
                        Map.entry("firstNames", "nombresCliente"),
                        Map.entry("lastNames", "apellidosCliente"),
                        Map.entry("birthDate", "fechaNacimiento"),
                        Map.entry("address", "direccionCliente"),
                        Map.entry("city", "ciudadCliente"),
                        Map.entry("phone", "telefonoCliente"),
                        Map.entry("email", "correoCliente"),
                        Map.entry("status", "estadoCliente"),
                        Map.entry("createdBy", "usuarioCreacion"),
                        Map.entry("createdDate", "fechaCreacion"),
                        Map.entry("lastModifiedBy", "usuarioModificacion"),
                        Map.entry("lastModifiedDate", "fechaModificacion"));
            } else {
                equivalencias = Map.of();
            }
            String atributo = equivalencias.getOrDefault(segmento, segmento);
            if (!ruta.isEmpty()) {
                ruta.append('.');
            }
            ruta.append(atributo);
            if (atributo.equals("cuenta")) {
                entidad = com.grupob.banquito.pasivos.entidad.Cuenta.class;
            } else if (atributo.equals("cliente")) {
                entidad = com.grupob.banquito.pasivos.entidad.Cliente.class;
            } else {
                entidad = null;
            }
        }
        return ruta.toString();
    }
}
