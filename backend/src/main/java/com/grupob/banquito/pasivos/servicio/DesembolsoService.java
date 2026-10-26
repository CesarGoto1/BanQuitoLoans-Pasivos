package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.SolicitudDesembolso;
import com.grupob.banquito.pasivos.dto.DesembolsoResultado;

public interface DesembolsoService {

    DesembolsoResultado acreditar(SolicitudDesembolso solicitud);
}
