package com.grupob.banquito.pasivos.dto;
import com.grupob.banquito.pasivos.entidad.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public record RespuestaDesembolso(
        UUID uuidTransaccion,
        String numeroPrestamo,
        String cuentaDestino,
        BigDecimal montoAcreditado,
        BigDecimal saldoPosterior,
        LocalDate fechaMovimiento) {
    public static RespuestaDesembolso desde(Movimiento movimiento) {
        return new RespuestaDesembolso(
                movimiento.getUuidTransaccion(),
                movimiento.getNumeroOperacion(),
                movimiento.getCuenta().getNumeroCuenta(),
                movimiento.getMontoMovimiento(),
                movimiento.getSaldoPosterior(),
                movimiento.getFechaMovimiento());
    }
}
