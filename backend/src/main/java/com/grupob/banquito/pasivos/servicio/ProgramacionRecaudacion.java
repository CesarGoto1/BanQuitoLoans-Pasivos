package com.grupob.banquito.pasivos.servicio;

import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
class ProgramacionRecaudacion {

    private static final ZoneId ZONA = ZoneId.of("America/Guayaquil");

    private final RecaudacionService recaudacionService;

    ProgramacionRecaudacion(RecaudacionService recaudacionService) {
        this.recaudacionService = recaudacionService;
    }

    @Scheduled(cron = "${recaudacion.programacion:-}", zone = "America/Guayaquil")
    void generarArchivoDelDia() {
        recaudacionService.generar(LocalDate.now(ZONA));
    }
}
