package com.grupob.banquito.liabilities.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "clientes")
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

    @Column(name = "usuarioCreacion")
    @NotNull
    @Size(max = 50)
    private String createdBy;

    @Column(name = "fechaCreacion")
    @NotNull
    private LocalDateTime createdDate;

    @Column(name = "usuarioModificacion")
    @Size(max = 50)
    private String lastModifiedBy;

    @Column(name = "fechaModificacion")
    private LocalDateTime lastModifiedDate;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EstadoCliente getStatus() {
        return status;
    }

    public void setStatus(EstadoCliente status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
