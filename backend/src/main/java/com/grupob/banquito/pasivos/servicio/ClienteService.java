package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCliente;

public interface ClienteService {

    RespuestaCliente buscarPorCedulaCliente(String cedulaCliente);

    RespuestaCliente buscarPorId(Integer idCliente);
}
