package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.ClienteResponse;
import com.grupob.banquito.liabilities.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponse findByIdentificationNumber(String identificationNumber) {
        return clienteRepository.findByIdentificationNumber(identificationNumber)
                .map(ClienteResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }

    @Override
    public ClienteResponse findById(Integer customerId) {
        return clienteRepository.findById(customerId)
                .map(ClienteResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
    }
}