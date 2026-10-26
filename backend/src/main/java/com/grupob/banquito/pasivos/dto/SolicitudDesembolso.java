package com.grupob.banquito.pasivos.dto;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
public record SolicitudDesembolso(
        @NotBlank(message = "La versión de la instrucción es obligatoria.")
        String version,
        @NotBlank(message = "El número de operación es obligatorio.")
        @Size(max = 20, message = "El número de operación admite hasta 20 caracteres.")
        String numeroPrestamo,
        @NotBlank(message = "La cédula es obligatoria.")
        @Pattern(regexp = "^[0-9]{10}$", message = "La cédula debe tener 10 dígitos numéricos.")
        String cedulaCliente,
        @NotBlank(message = "La cuenta de destino es obligatoria.")
        String cuentaDestino,
        @NotNull(message = "El monto aprobado es obligatorio.")
        @Positive(message = "El monto aprobado debe ser mayor que cero.")
        @Digits(integer = 10, fraction = 2, message = "El monto aprobado admite dos decimales.")
        BigDecimal montoAprobado,
        @NotNull(message = "La contribución SOLCA es obligatoria.")
        @PositiveOrZero(message = "La contribución SOLCA no puede ser negativa.")
        @Digits(integer = 10, fraction = 2, message = "La contribución SOLCA admite dos decimales.")
        BigDecimal contribucionSolca,
        @NotNull(message = "El monto neto es obligatorio.")
        @Positive(message = "El monto neto debe ser mayor que cero.")
        @Digits(integer = 10, fraction = 2, message = "El monto neto admite dos decimales.")
        BigDecimal montoNeto,
        @NotNull(message = "La fecha de desembolso es obligatoria.")
        LocalDate fechaDesembolso,
        @Size(max = 100, message = "La referencia admite hasta 100 caracteres.")
        String referencia) {
}
