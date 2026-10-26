package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCuenta;
import com.grupob.banquito.pasivos.repositorio.ClienteRepository;
import com.grupob.banquito.pasivos.repositorio.CuentaRepository;
import java.util.List;
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
    public List<RespuestaCuenta> buscarPorCedulaCliente(String cedulaCliente) {
        clienteRepositorio.findByCedulaCliente(cedulaCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
        return cuentaRepositorio.findByClienteCedulaCliente(cedulaCliente).stream()
                .map(RespuestaCuenta::desde)
                .toList();
    }
}