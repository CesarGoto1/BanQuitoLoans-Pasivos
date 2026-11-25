package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaDebito;
import com.grupob.banquito.pasivos.dto.SolicitudDebito;

public interface DebitoService {

    RespuestaDebito solicitar(SolicitudDebito solicitud);
}
