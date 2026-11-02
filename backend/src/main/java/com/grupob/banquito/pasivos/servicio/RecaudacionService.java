package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.ArchivoRecaudacion;
import java.time.LocalDate;

public interface RecaudacionService {

    ArchivoRecaudacion generar(LocalDate fechaProceso);
}
