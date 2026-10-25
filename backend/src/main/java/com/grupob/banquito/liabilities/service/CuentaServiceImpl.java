package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.CuentaResponse;
import com.grupob.banquito.liabilities.repository.ClienteRepository;
import com.grupob.banquito.liabilities.repository.CuentaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class CuentaServiceImpl implements CuentaService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public CuentaServiceImpl(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<CuentaResponse> findByCustomerIdentificationNumber(String identificationNumber) {
        clienteRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
        return cuentaRepository.findByCustomerIdentificationNumber(identificationNumber).stream()
                .map(CuentaResponse::from)
                .toList();
    }
}