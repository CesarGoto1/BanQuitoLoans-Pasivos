package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCuenta;
import java.util.List;

public interface CuentaService {

    List<RespuestaCuenta> buscarPorCedulaCliente(String cedulaCliente);
}
