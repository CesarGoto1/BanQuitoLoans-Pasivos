package com.grupob.banquito.pasivos.entidad;

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
    private Integer idCliente;

    @Column(name = "cedulaCliente")
    @NotNull
    @Size(max = 10)
    private String cedulaCliente;

    @Column(name = "nombresCliente")
    @NotNull
    @Size(max = 100)
    private String nombresCliente;

    @Column(name = "apellidosCliente")
    @NotNull
    @Size(max = 100)
    private String apellidosCliente;

    @Column(name = "fechaNacimiento")
    @NotNull
    private LocalDate fechaNacimiento;

    @Column(name = "direccionCliente")
    @NotNull
    @Size(max = 200)
    private String direccionCliente;

    @Column(name = "ciudadCliente")
    @NotNull
    @Size(max = 50)
    private String ciudadCliente;

    @Column(name = "telefonoCliente")
    @NotNull
    @Size(max = 10)
    private String telefonoCliente;

    @Column(name = "correoCliente")
    @Size(max = 100)
    private String correoCliente;

    @Column(name = "estadoCliente")
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoCliente estadoCliente;

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

    public Cliente() {
    }

    public Cliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getCiudadCliente() {
        return ciudadCliente;
    }

    public void setCiudadCliente(String ciudadCliente) {
        this.ciudadCliente = ciudadCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public EstadoCliente getEstadoCliente() {
        return estadoCliente;
    }

    public void setEstadoCliente(EstadoCliente estadoCliente) {
        this.estadoCliente = estadoCliente;
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
