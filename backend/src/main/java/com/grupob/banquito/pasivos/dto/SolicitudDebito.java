package com.grupob.banquito.pasivos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SolicitudDebito(
        @NotBlank String numeroOperacion,
        @NotBlank String identificacionCliente,
        @NotBlank String cuentaPrincipal,
        @NotNull @DecimalMin(value = "0.01") BigDecimal montoSolicitado,
        boolean permitirDebitoParcial,
        boolean permitirBarridoMulticuenta) {
}
