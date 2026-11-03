package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCuenta;
import com.grupob.banquito.pasivos.repositorio.ClienteRepository;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class CuentaServiceImpl implements CuentaService {

    private final ClienteRepository clienteRepositorio;
    private final CuentaRepository cuentaRepositorio;

    public CuentaServiceImpl(ClienteRepository clienteRepositorio, CuentaRepository cuentaRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.cuentaRepositorio = cuentaRepositorio;
    }

    @Override
    public Page<RespuestaCuenta> buscarPorCedulaCliente(String cedulaCliente, Pageable paginacion) {
        clienteRepositorio.findByCedulaCliente(cedulaCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
        return cuentaRepositorio.findByClienteCedulaCliente(cedulaCliente, paginacion)
                .map(RespuestaCuenta::desde);
    }
}