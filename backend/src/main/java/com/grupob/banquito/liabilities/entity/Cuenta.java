package com.grupob.banquito.liabilities.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "cuentas")
@EntityListeners(AuditingEntityListener.class)
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuenta")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    @NotNull
    private Cliente customer;

    @Column(name = "numeroCuenta")
    @NotNull
    @Size(max = 20)
    private String accountNumber;

    @Column(name = "tipoCuenta")
    @NotNull
    @Size(max = 9)
    private String type;

    @Column(name = "saldoCuenta")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @Column(name = "fechaApertura")
    @NotNull
    private LocalDate openingDate;

    @Column(name = "estadoCuenta")
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoCuenta status;

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

    protected Cuenta() {
    }

    public Integer getId() {
        return id;
    }

    public Cliente getCustomer() {
        return customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public EstadoCuenta getStatus() {
        return status;
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
