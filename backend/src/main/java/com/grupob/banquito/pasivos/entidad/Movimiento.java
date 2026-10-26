package com.grupob.banquito.pasivos.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMovimiento")
    private Integer idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCuenta")
    @NotNull
    private Cuenta cuenta;

    @Column(name = "tipoMovimiento")
    @NotNull
    @Size(max = 7)
    private String tipoMovimiento;

    @Column(name = "conceptoMovimiento")
    @NotNull
    @Size(max = 20)
    private String conceptoMovimiento;

    @Column(name = "montoMovimiento")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal montoMovimiento;

    @Column(name = "saldoPosterior")
    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal saldoPosterior;

    @Column(name = "fechaMovimiento")
    @NotNull
    private LocalDate fechaMovimiento;

    @Column(name = "fechaHoraMovimiento")
    @NotNull
    private LocalDateTime fechaHoraMovimiento;

    @Column(name = "uuidTransaccion")
    @NotNull
    private UUID uuidTransaccion;

    @Column(name = "numeroOperacion")
    @Size(max = 20)
    private String numeroOperacion;

    @Column(name = "canal")
    @Size(max = 20)
    private String canal;

    @Column(name = "referencia")
    @Size(max = 100)
    private String referencia;

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

    public Movimiento() {
    }

    public Movimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getConceptoMovimiento() {
        return conceptoMovimiento;
    }

    public void setConceptoMovimiento(String conceptoMovimiento) {
        this.conceptoMovimiento = conceptoMovimiento;
    }

    public BigDecimal getMontoMovimiento() {
        return montoMovimiento;
    }

    public void setMontoMovimiento(BigDecimal montoMovimiento) {
        this.montoMovimiento = montoMovimiento;
    }

    public BigDecimal getSaldoPosterior() {
        return saldoPosterior;
    }

    public void setSaldoPosterior(BigDecimal saldoPosterior) {
        this.saldoPosterior = saldoPosterior;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public LocalDateTime getFechaHoraMovimiento() {
        return fechaHoraMovimiento;
    }

    public void setFechaHoraMovimiento(LocalDateTime fechaHoraMovimiento) {
        this.fechaHoraMovimiento = fechaHoraMovimiento;
    }

    public UUID getUuidTransaccion() {
        return uuidTransaccion;
    }

    public void setUuidTransaccion(UUID uuidTransaccion) {
        this.uuidTransaccion = uuidTransaccion;
    }

    public String getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setNumeroOperacion(String numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
