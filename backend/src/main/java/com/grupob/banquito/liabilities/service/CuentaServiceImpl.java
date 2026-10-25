package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.CuentaResponse;
import com.grupob.banquito.liabilities.exception.ClienteNoEncontradoException;
import com.grupob.banquito.liabilities.repository.CuentaRepository;
import com.grupob.banquito.liabilities.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(ClienteNoEncontradoException::new);
        return cuentaRepository.findByCustomerIdentificationNumber(identificationNumber).stream()
                .map(CuentaResponse::from)
                .toList();
    }
}
