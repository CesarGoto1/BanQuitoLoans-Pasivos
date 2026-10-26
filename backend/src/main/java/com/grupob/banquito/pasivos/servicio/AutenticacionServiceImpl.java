package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaInicioSesion;
import com.grupob.banquito.pasivos.repositorio.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AutenticacionServiceImpl implements AutenticacionService {

    private final ClienteRepository clienteRepositorio;

    public AutenticacionServiceImpl(ClienteRepository clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Override
    public RespuestaInicioSesion iniciarSesion(String cedulaCliente) {
        return clienteRepositorio.findByCedulaCliente(cedulaCliente)
                .map(RespuestaInicioSesion::desde)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }
}