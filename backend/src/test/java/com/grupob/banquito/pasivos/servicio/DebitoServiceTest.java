package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaDebito;
import com.grupob.banquito.pasivos.dto.SolicitudDebito;
import com.grupob.banquito.pasivos.entidad.Cliente;
import com.grupob.banquito.pasivos.entidad.Cuenta;
import com.grupob.banquito.pasivos.entidad.EstadoCliente;
import com.grupob.banquito.pasivos.entidad.EstadoCuenta;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import com.grupob.banquito.pasivos.repositorio.MovimientoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DebitoServiceTest {

    @Mock
    private CuentaRepository cuentaRepositorio;

    @Mock
    private MovimientoRepository movimientoRepositorio;

    @InjectMocks
    private DebitoServiceImpl servicio;

    @Test
    void cobraLaCuotaUsandoLaCuentaPrincipalYElBarridoSecundario() {
        Cliente cliente = cliente("1712345678");
        Cuenta principal = cuenta(cliente, "1002003001", "500.00");
        Cuenta secundaria = cuenta(cliente, "1002003005", "500.00");
        org.mockito.Mockito.when(cuentaRepositorio
                .findByClienteCedulaClienteAndEstadoCuentaOrderByNumeroCuenta("1712345678", EstadoCuenta.ACTIVA))
                .thenReturn(List.of(principal, secundaria));

        RespuestaDebito respuesta = servicio.solicitar(new SolicitudDebito(
                "LOAN-2026-0089", "1712345678", "1002003001",
                new BigDecimal("911.44"), true, true));

        assertThat(respuesta.estado()).isEqualTo(RespuestaDebito.EstadoDebito.PAGADA);
        assertThat(respuesta.montoCobradoTotal()).isEqualByComparingTo("911.44");
        assertThat(respuesta.montoPendiente()).isZero();
        assertThat(respuesta.desgloseCobros()).hasSize(2);
        assertThat(principal.getSaldoCuenta()).isEqualByComparingTo("0.00");
        assertThat(secundaria.getSaldoCuenta()).isEqualByComparingTo("88.56");
        verify(movimientoRepositorio, org.mockito.Mockito.times(2)).save(any());
    }

    @Test
    void registraPagoParcialYProgramaReintento() {
        Cliente cliente = cliente("1712345679");
        Cuenta principal = cuenta(cliente, "1002003010", "200.00");
        org.mockito.Mockito.when(cuentaRepositorio
                .findByClienteCedulaClienteAndEstadoCuentaOrderByNumeroCuenta("1712345679", EstadoCuenta.ACTIVA))
                .thenReturn(List.of(principal));

        RespuestaDebito respuesta = servicio.solicitar(new SolicitudDebito(
                "LOAN-2026-0090", "1712345679", "1002003010",
                new BigDecimal("911.44"), true, true));

        assertThat(respuesta.estado()).isEqualTo(RespuestaDebito.EstadoDebito.PARCIAL);
        assertThat(respuesta.montoCobradoTotal()).isEqualByComparingTo("200.00");
        assertThat(respuesta.montoPendiente()).isEqualByComparingTo("711.44");
        assertThat(respuesta.proximoReintentoEnMinutos()).isEqualTo(60);
        assertThat(principal.getSaldoCuenta()).isEqualByComparingTo("0.00");
        verify(movimientoRepositorio).save(any());
    }

    private Cliente cliente(String cedula) {
        Cliente cliente = new Cliente(1);
        cliente.setCedulaCliente(cedula);
        cliente.setEstadoCliente(EstadoCliente.ACTIVO);
        return cliente;
    }

    private Cuenta cuenta(Cliente cliente, String numero, String saldo) {
        Cuenta cuenta = new Cuenta(1);
        cuenta.setCliente(cliente);
        cuenta.setNumeroCuenta(numero);
        cuenta.setEstadoCuenta(EstadoCuenta.ACTIVA);
        cuenta.setSaldoCuenta(new BigDecimal(saldo));
        return cuenta;
    }
}
