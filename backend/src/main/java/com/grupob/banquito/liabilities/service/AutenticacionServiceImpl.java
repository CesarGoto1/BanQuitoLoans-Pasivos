package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.InicioSesionResponse;
import com.grupob.banquito.liabilities.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AutenticacionServiceImpl implements AutenticacionService {

    private final ClienteRepository clienteRepository;

    public AutenticacionServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public InicioSesionResponse login(String identificationNumber) {
        return clienteRepository.findByIdentificationNumber(identificationNumber)
                .map(InicioSesionResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }
}