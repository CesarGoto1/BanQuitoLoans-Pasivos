package com.grupob.banquito.liabilities.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "movimientos")
@EntityListeners(AuditingEntityListener.class)
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimiento")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCuenta")
    @NotNull
    private Cuenta account;

    @Column(name = "tipoMovimiento")
    @NotNull
    @Size(max = 7)
    private String type;

    @Column(name = "conceptoMovimiento")
    @NotNull
    @Size(max = 20)
    private String concept;

    @Column(name = "montoMovimiento")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @Column(name = "saldoPosterior")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balanceAfter;

    @Column(name = "fechaMovimiento")
    @NotNull
    private LocalDate movementDate;

    @Column(name = "fechaHoraMovimiento")
    @NotNull
    private LocalDateTime movementDateTime;

    @Column(name = "uuidTransaccion")
    @NotNull
    private UUID transactionUuid;

    @Column(name = "numeroOperacion")
    @Size(max = 20)
    private String operationNumber;

    @Column(name = "canal")
    @Size(max = 20)
    private String channel;

    @Column(name = "referencia")
    @Size(max = 100)
    private String reference;

    @CreatedBy
    @Column(name = "usuarioCreacion")
    @NotNull
    @Size(max = 50)
    private String createdBy;

    @CreatedDate
    @Column(name = "fechaCreacion")
    @NotNull
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "usuarioModificacion")
    @Size(max = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "fechaModificacion")
    private LocalDateTime lastModifiedDate;

    protected Movimiento() {
    }

    public Integer getId() {
        return id;
    }

    public Cuenta getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public String getConcept() {
        return concept;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDate getMovementDate() {
        return movementDate;
    }

    public LocalDateTime getMovementDateTime() {
        return movementDateTime;
    }

    public UUID getTransactionUuid() {
        return transactionUuid;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public String getChannel() {
        return channel;
    }

    public String getReference() {
        return reference;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
}
