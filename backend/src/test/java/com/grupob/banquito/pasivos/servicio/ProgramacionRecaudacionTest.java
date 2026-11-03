package com.grupob.banquito.pasivos.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProgramacionRecaudacionTest {

    @Test
    void generaElArchivoDeLaFechaActualDeGuayaquil() {
        RecaudacionService servicio = mock(RecaudacionService.class);

        new ProgramacionRecaudacion(servicio).generarArchivoDelDia();

        ArgumentCaptor<LocalDate> fecha = ArgumentCaptor.forClass(LocalDate.class);
        verify(servicio).generar(fecha.capture());
        assertThat(fecha.getValue()).isEqualTo(LocalDate.now(ZoneId.of("America/Guayaquil")));
    }
}
