package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.RespuestaDebito;
import com.grupob.banquito.pasivos.dto.SolicitudDebito;
import com.grupob.banquito.pasivos.servicio.DebitoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pasivos/debitos")
public class DebitoController {

    private final DebitoService debitoServicio;

    public DebitoController(DebitoService debitoServicio) {
        this.debitoServicio = debitoServicio;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<RespuestaDebito> solicitar(@Valid @RequestBody SolicitudDebito solicitud) {
        return ResponseEntity.ok(debitoServicio.solicitar(solicitud));
    }
}
