package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaInicioSesion;

public interface AutenticacionService {

    RespuestaInicioSesion iniciarSesion(String cedulaCliente);
}
