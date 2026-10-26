package com.grupob.banquito.pasivos;

import com.grupob.banquito.pasivos.dto.*;
import com.grupob.banquito.pasivos.entidad.*;
import com.grupob.banquito.pasivos.repositorio.*;
import com.grupob.banquito.pasivos.servicio.*;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.domain.*;
import tools.jackson.databind.json.JsonMapper;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompatibilidadTraduccionTest {
    @Test
    void atributosCoincidenConColumnasYConsultasDerivadasSiguenSiendoValidas() {
        for (Class<?> entidad : List.of(Cliente.class, Cuenta.class, Movimiento.class)) {
            for (var atributo : entidad.getDeclaredFields()) {
                Column columna = atributo.getAnnotation(Column.class);
                if (columna != null) {
                    assertEquals(columna.name(), atributo.getName());
                }
            }
        }
        Map<Class<?>, Class<?>> repositorios = Map.of(
                ClienteRepository.class, Cliente.class,
                CuentaRepository.class, Cuenta.class,
                MovimientoRepository.class, Movimiento.class);
        repositorios.forEach((repositorio, entidad) -> {
            for (var metodo : repositorio.getDeclaredMethods()) {
                assertDoesNotThrow(() -> new PartTree(metodo.getName(), entidad));
            }
        });
        assertTrue(new PartTree("findFirstByNumeroOperacionAndConceptoMovimiento", Movimiento.class).isLimiting());
    }

    @Test
    void contratosJsonUsanLosNombresEnEspanol() throws Exception {
        var conversor = JsonMapper.builder().build();
        Map<Class<?>, String> contratos = Map.of(
                RespuestaCliente.class, "idCliente cedulaCliente nombresCliente apellidosCliente fechaNacimiento direccionCliente ciudadCliente telefonoCliente correoCliente estado",
                RespuestaCuenta.class, "numeroCuenta tipo saldoCuenta fechaApertura estado",
                RespuestaInicioSesion.class, "idCliente cedulaCliente nombresCliente apellidosCliente estado",
                RespuestaMovimiento.class, "uuidTransaccion tipo conceptoMovimiento montoMovimiento saldoPosterior fechaMovimiento fechaHoraMovimiento numeroOperacion canal referencia",
                RespuestaDesembolso.class, "uuidTransaccion numeroPrestamo cuentaDestino montoAcreditado saldoPosterior fechaMovimiento");
        for (var contrato : contratos.entrySet()) {
            var componentes = contrato.getKey().getRecordComponents();
            Class<?>[] tipos = Arrays.stream(componentes).map(c -> c.getType()).toArray(Class<?>[]::new);
            Object respuesta = contrato.getKey().getDeclaredConstructor(tipos).newInstance(new Object[tipos.length]);
            Map<?, ?> json = conversor.readValue(conversor.writeValueAsString(respuesta), Map.class);
            assertEquals(Set.of(contrato.getValue().split(" ")), json.keySet());
        }
        var inicio = conversor.readValue("{\"cedulaCliente\":\"1712345678\"}", SolicitudInicioSesion.class);
        assertEquals("1712345678", inicio.cedulaCliente());
        String instruccion = """
                {"version":"1.0","numeroPrestamo":"PRE-001","cedulaCliente":"1712345678",
                "cuentaDestino":"001","montoAprobado":1000.00,"contribucionSolca":5.00,
                "montoNeto":995.00,"fechaDesembolso":"2026-10-25","referencia":"Prueba"}
                """;
        var solicitud = conversor.readValue(instruccion, SolicitudDesembolso.class);
        assertEquals("PRE-001", solicitud.numeroPrestamo());
        assertEquals(new BigDecimal("995.00"), solicitud.montoNeto());
        assertEquals(LocalDate.of(2026, 10, 25), solicitud.fechaDesembolso());
        assertEquals(conversor.readTree(instruccion), conversor.readTree(conversor.writeValueAsString(solicitud)));
    }

    @Test
    void ordenamientoAnteriorSeTraduceSinCambiarMetadatosDePagina() {
        var cuentas = mock(CuentaRepository.class);
        var movimientos = mock(MovimientoRepository.class);
        var servicio = new MovimientoServiceImpl(cuentas, movimientos);
        var original = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("movementDate"), Sort.Order.desc("id"),
                Sort.Order.asc("account.customer.identificationNumber")));
        var traducida = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("fechaMovimiento"), Sort.Order.desc("idMovimiento"),
                Sort.Order.asc("cuenta.cliente.cedulaCliente")));
        when(cuentas.findByNumeroCuenta("001")).thenReturn(Optional.of(new Cuenta()));
        when(movimientos.findByCuentaNumeroCuenta("001", traducida)).thenReturn(new PageImpl<>(List.of(), traducida, 0));
        var resultado = servicio.buscarPorNumeroCuenta("001", original);
        verify(movimientos).findByCuentaNumeroCuenta("001", traducida);
        assertEquals(original, resultado.getPageable());
    }

    @Test
    void desembolsoAcreditaElNetoUnaSolaVez() {
        var cuentas = mock(CuentaRepository.class);
        var movimientos = mock(MovimientoRepository.class);
        var servicio = new DesembolsoServiceImpl(cuentas, movimientos);
        var cliente = new Cliente(1);
        cliente.setCedulaCliente("1712345678");
        cliente.setEstadoCliente(EstadoCliente.ACTIVO);
        var cuenta = new Cuenta(1);
        cuenta.setCliente(cliente);
        cuenta.setNumeroCuenta("001");
        cuenta.setEstadoCuenta(EstadoCuenta.ACTIVA);
        cuenta.setSaldoCuenta(new BigDecimal("100.00"));
        var solicitud = new SolicitudDesembolso("1.0", "PRE-001", "1712345678", "001",
                new BigDecimal("1000.00"), new BigDecimal("5.00"), new BigDecimal("995.00"),
                LocalDate.of(2026, 10, 25), "Prueba");
        when(cuentas.findParaActualizarByNumeroCuenta("001")).thenReturn(Optional.of(cuenta));
        when(movimientos.findFirstByNumeroOperacionAndConceptoMovimiento("PRE-001", "DESEMBOLSO"))
                .thenReturn(Optional.empty());
        var primero = servicio.acreditar(solicitud);
        assertTrue(primero.creado());
        assertEquals(new BigDecimal("1095.00"), cuenta.getSaldoCuenta());
        var captor = org.mockito.ArgumentCaptor.forClass(Movimiento.class);
        verify(movimientos).save(captor.capture());
        var movimiento = captor.getValue();
        assertEquals("CREDITO", movimiento.getTipoMovimiento());
        assertEquals("DESEMBOLSO", movimiento.getConceptoMovimiento());
        assertEquals(solicitud.montoNeto(), movimiento.getMontoMovimiento());
        when(movimientos.findFirstByNumeroOperacionAndConceptoMovimiento("PRE-001", "DESEMBOLSO"))
                .thenReturn(Optional.of(movimiento));
        var repetido = servicio.acreditar(solicitud);
        assertFalse(repetido.creado());
        assertEquals(primero.desembolso(), repetido.desembolso());
        assertEquals(new BigDecimal("1095.00"), cuenta.getSaldoCuenta());
        verify(movimientos, times(1)).save(any(Movimiento.class));
    }
}
