package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.RespuestaCliente;
import com.grupob.banquito.pasivos.repositorio.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepositorio;

    public ClienteServiceImpl(ClienteRepository clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Override
    public RespuestaCliente buscarPorCedulaCliente(String cedulaCliente) {
        return clienteRepositorio.findByCedulaCliente(cedulaCliente)
                .map(RespuestaCliente::desde)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }

    @Override
    public RespuestaCliente buscarPorId(Integer idCliente) {
        return clienteRepositorio.findById(idCliente)
                .map(RespuestaCliente::desde)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }
}