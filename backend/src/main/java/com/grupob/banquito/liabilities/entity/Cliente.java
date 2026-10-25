package com.grupob.banquito.liabilities.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "clientes")
@EntityListeners(AuditingEntityListener.class)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Integer id;

    @Column(name = "cedulaCliente")
    @NotNull
    @Size(max = 10)
    private String identificationNumber;

    @Column(name = "nombresCliente")
    @NotNull
    @Size(max = 100)
    private String firstNames;

    @Column(name = "apellidosCliente")
    @NotNull
    @Size(max = 100)
    private String lastNames;

    @Column(name = "fechaNacimiento")
    @NotNull
    private LocalDate birthDate;

    @Column(name = "direccionCliente")
    @NotNull
    @Size(max = 200)
    private String address;

    @Column(name = "ciudadCliente")
    @NotNull
    @Size(max = 50)
    private String city;

    @Column(name = "telefonoCliente")
    @NotNull
    @Size(max = 10)
    private String phone;

    @Column(name = "correoCliente")
    @Size(max = 100)
    private String email;

    @Column(name = "estadoCliente")
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoCliente status;

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

    protected Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public EstadoCliente getStatus() {
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
