package com.grupob.banquito.pasivos.dto;
import com.grupob.banquito.pasivos.entidad.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
public record RespuestaMovimiento(
        UUID uuidTransaccion,
        String tipo,
        String conceptoMovimiento,
        BigDecimal montoMovimiento,
        BigDecimal saldoPosterior,
        LocalDate fechaMovimiento,
        LocalDateTime fechaHoraMovimiento,
        String numeroOperacion,
        String canal,
        String referencia) {
    public static RespuestaMovimiento desde(Movimiento movimiento) {
        return new RespuestaMovimiento(
                movimiento.getUuidTransaccion(),
                movimiento.getTipoMovimiento(),
                movimiento.getConceptoMovimiento(),
                movimiento.getMontoMovimiento(),
                movimiento.getSaldoPosterior(),
                movimiento.getFechaMovimiento(),
                movimiento.getFechaHoraMovimiento(),
                movimiento.getNumeroOperacion(),
                movimiento.getCanal(),
                movimiento.getReferencia());
    }
}
