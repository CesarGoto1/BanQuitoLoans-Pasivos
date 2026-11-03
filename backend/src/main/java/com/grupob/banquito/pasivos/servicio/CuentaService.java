package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CuentaService {

    Page<RespuestaCuenta> buscarPorCedulaCliente(String cedulaCliente, Pageable paginacion);
}
