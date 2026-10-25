package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.InicioSesionResponse;
import com.grupob.banquito.liabilities.exception.ClienteNoEncontradoException;
import com.grupob.banquito.liabilities.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AutenticacionServiceImpl implements AutenticacionService {

    private static final Logger log = LoggerFactory.getLogger(AutenticacionServiceImpl.class);

    private final ClienteRepository clienteRepository;

    public AutenticacionServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public InicioSesionResponse login(String identificationNumber) {
        String masked = maskIdentification(identificationNumber);
        return clienteRepository.findByIdentificationNumber(identificationNumber)
                .map(customer -> {
                    log.info("Login accepted for identification {}", masked);
                    return InicioSesionResponse.from(customer);
                })
                .orElseThrow(() -> {
                    log.warn("Login rejected for identification {}", masked);
                    return new ClienteNoEncontradoException();
                });
    }

    private String maskIdentification(String identificationNumber) {
        if (identificationNumber == null || identificationNumber.length() < 4) {
            return "****";
        }
        return identificationNumber.substring(0, 2)
                + "*".repeat(identificationNumber.length() - 4)
                + identificationNumber.substring(identificationNumber.length() - 2);
    }
}
