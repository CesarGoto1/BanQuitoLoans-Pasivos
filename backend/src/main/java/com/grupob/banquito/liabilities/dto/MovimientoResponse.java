package com.grupob.banquito.liabilities.dto;

import com.grupob.banquito.liabilities.entity.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientoResponse(
        UUID transactionUuid,
        String type,
        String concept,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDate movementDate,
        LocalDateTime movementDateTime,
        String operationNumber,
        String channel,
        String reference) {

    public static MovimientoResponse from(Movimiento movement) {
        return new MovimientoResponse(
                movement.getTransactionUuid(),
                movement.getType(),
                movement.getConcept(),
                movement.getAmount(),
                movement.getBalanceAfter(),
                movement.getMovementDate(),
                movement.getMovementDateTime(),
                movement.getOperationNumber(),
                movement.getChannel(),
                movement.getReference());
    }
}
