package com.grupob.banquito.liabilities.dto;

import com.grupob.banquito.liabilities.entity.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DesembolsoResponse(
        UUID transactionUuid,
        String loanNumber,
        String destinationAccount,
        BigDecimal creditedAmount,
        BigDecimal balanceAfter,
        LocalDate movementDate) {

    public static DesembolsoResponse from(Movimiento movement) {
        return new DesembolsoResponse(
                movement.getTransactionUuid(),
                movement.getOperationNumber(),
                movement.getAccount().getAccountNumber(),
                movement.getAmount(),
                movement.getBalanceAfter(),
                movement.getMovementDate());
    }
}
