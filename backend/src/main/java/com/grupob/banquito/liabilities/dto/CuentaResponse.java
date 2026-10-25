package com.grupob.banquito.liabilities.dto;

import com.grupob.banquito.liabilities.entity.Cuenta;
import com.grupob.banquito.liabilities.entity.EstadoCuenta;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CuentaResponse(
        String accountNumber,
        String type,
        BigDecimal balance,
        LocalDate openingDate,
        EstadoCuenta status) {

    public static CuentaResponse from(Cuenta account) {
        return new CuentaResponse(
                account.getAccountNumber(),
                account.getType(),
                account.getBalance(),
                account.getOpeningDate(),
                account.getStatus());
    }
}
