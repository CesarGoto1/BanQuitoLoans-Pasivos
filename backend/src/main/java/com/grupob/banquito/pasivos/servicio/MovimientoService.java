package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaMovimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovimientoService {

    Page<RespuestaMovimiento> buscarPorNumeroCuenta(String numeroCuenta, Pageable paginacion);
}
