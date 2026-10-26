package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.SolicitudDesembolso;
import com.grupob.banquito.pasivos.dto.RespuestaDesembolso;
import com.grupob.banquito.pasivos.dto.DesembolsoResultado;
import com.grupob.banquito.pasivos.servicio.DesembolsoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/desembolsos")
public class DesembolsoController {

    private final DesembolsoService desembolsoServicio;

    public DesembolsoController(DesembolsoService desembolsoServicio) {
        this.desembolsoServicio = desembolsoServicio;
    }

    @PostMapping
    public ResponseEntity<RespuestaDesembolso> acreditar(@Valid @RequestBody SolicitudDesembolso solicitud) {
        DesembolsoResultado resultado = desembolsoServicio.acreditar(solicitud);
        return ResponseEntity.status(resultado.creado() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(resultado.desembolso());
    }
}
