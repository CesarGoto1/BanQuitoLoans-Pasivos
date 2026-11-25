package com.grupob.banquito.pasivos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RespuestaDebito(
        UUID uuidTransaccion,
        EstadoDebito estado,
        BigDecimal montoSolicitado,
        BigDecimal montoCobradoTotal,
        BigDecimal montoPendiente,
        List<CobroCuenta> desgloseCobros,
        Integer proximoReintentoEnMinutos,
        LocalDateTime fechaHoraProcesamiento) {

    public enum EstadoDebito {
        PAGADA,
        PARCIAL,
        SIN_FONDOS
    }

    public record CobroCuenta(String cuenta, BigDecimal montoCobrado, String tipo) {
    }
}
