package com.grupob.banquito.liabilities.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DesembolsoRequest(
        @NotBlank(message = "La versión de la instrucción es obligatoria.")
        String version,
        @NotBlank(message = "El número de operación es obligatorio.")
        @Size(max = 20, message = "El número de operación admite hasta 20 caracteres.")
        String loanNumber,
        @NotBlank(message = "La cédula es obligatoria.")
        @Pattern(regexp = "^[0-9]{10}$", message = "La cédula debe tener 10 dígitos numéricos.")
        String customerNationalId,
        @NotBlank(message = "La cuenta de destino es obligatoria.")
        String destinationAccount,
        @NotNull(message = "El monto aprobado es obligatorio.")
        @Positive(message = "El monto aprobado debe ser mayor que cero.")
        @Digits(integer = 10, fraction = 2, message = "El monto aprobado admite dos decimales.")
        BigDecimal approvedAmount,
        @NotNull(message = "La contribución SOLCA es obligatoria.")
        @PositiveOrZero(message = "La contribución SOLCA no puede ser negativa.")
        @Digits(integer = 10, fraction = 2, message = "La contribución SOLCA admite dos decimales.")
        BigDecimal solcaContribution,
        @NotNull(message = "El monto neto es obligatorio.")
        @Positive(message = "El monto neto debe ser mayor que cero.")
        @Digits(integer = 10, fraction = 2, message = "El monto neto admite dos decimales.")
        BigDecimal netAmount,
        @NotNull(message = "La fecha de desembolso es obligatoria.")
        LocalDate disbursementDate,
        @Size(max = 100, message = "La referencia admite hasta 100 caracteres.")
        String reference) {
}
