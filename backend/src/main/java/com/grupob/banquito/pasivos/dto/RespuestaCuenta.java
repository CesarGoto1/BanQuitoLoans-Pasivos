package com.grupob.banquito.pasivos.dto;
import com.grupob.banquito.pasivos.entidad.Cuenta;
import com.grupob.banquito.pasivos.entidad.EstadoCuenta;
import java.math.BigDecimal;
import java.time.LocalDate;
public record RespuestaCuenta(
        String numeroCuenta,
        String tipo,
        BigDecimal saldoCuenta,
        LocalDate fechaApertura,
        EstadoCuenta estado) {
    public static RespuestaCuenta desde(Cuenta cuenta) {
        return new RespuestaCuenta(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoCuenta(),
                cuenta.getFechaApertura(),
                cuenta.getEstadoCuenta());
    }
}
