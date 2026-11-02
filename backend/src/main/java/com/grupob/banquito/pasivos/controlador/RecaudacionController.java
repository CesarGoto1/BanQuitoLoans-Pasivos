package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.ArchivoRecaudacion;
import com.grupob.banquito.pasivos.servicio.RecaudacionService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recaudaciones")
public class RecaudacionController {

    private final RecaudacionService recaudacionService;

    public RecaudacionController(RecaudacionService recaudacionService) {
        this.recaudacionService = recaudacionService;
    }

    @GetMapping("/archivo")
    public ResponseEntity<byte[]> generar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaProceso) {
        ArchivoRecaudacion archivo = recaudacionService.generar(fechaProceso);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(archivo.nombreArchivo()).build());
        return ResponseEntity.ok().headers(headers).body(archivo.contenido());
    }
}
