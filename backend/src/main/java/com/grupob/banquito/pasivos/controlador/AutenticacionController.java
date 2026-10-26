package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.SolicitudInicioSesion;
import com.grupob.banquito.pasivos.dto.RespuestaInicioSesion;
import com.grupob.banquito.pasivos.servicio.AutenticacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacion")
public class AutenticacionController {

    private final AutenticacionService autenticacionServicio;

    public AutenticacionController(AutenticacionService autenticacionServicio) {
        this.autenticacionServicio = autenticacionServicio;
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaInicioSesion> iniciarSesion(@Valid @RequestBody SolicitudInicioSesion solicitud) {
        return ResponseEntity.ok(autenticacionServicio.iniciarSesion(solicitud.cedulaCliente()));
    }
}
