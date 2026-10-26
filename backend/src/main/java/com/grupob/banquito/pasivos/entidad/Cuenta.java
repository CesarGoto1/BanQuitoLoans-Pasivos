package com.grupob.banquito.pasivos.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuenta")
    private Integer idCuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    @NotNull
    private Cliente cliente;

    @Column(name = "numeroCuenta")
    @NotNull
    @Size(max = 20)
    private String numeroCuenta;

    @Column(name = "tipoCuenta")
    @NotNull
    @Size(max = 9)
    private String tipoCuenta;

    @Column(name = "saldoCuenta")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal saldoCuenta;

    @Column(name = "fechaApertura")
    @NotNull
    private LocalDate fechaApertura;

    @Column(name = "estadoCuenta")
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoCuenta estadoCuenta;

    @Column(name = "usuarioCreacion")
    @NotNull
    @Size(max = 50)
    private String usuarioCreacion;

    @Column(name = "fechaCreacion")
    @NotNull
    private LocalDateTime fechaCreacion;

    @Column(name = "usuarioModificacion")
    @Size(max = 50)
    private String usuarioModificacion;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;

    public Cuenta() {
    }

    public Cuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoCuenta() {
        return saldoCuenta;
    }

    public void setSaldoCuenta(BigDecimal saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
