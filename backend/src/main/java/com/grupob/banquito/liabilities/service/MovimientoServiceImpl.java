package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.MovimientoResponse;
import com.grupob.banquito.liabilities.repository.CuentaRepository;
import com.grupob.banquito.liabilities.repository.MovimientoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MovimientoServiceImpl implements MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public MovimientoServiceImpl(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public Page<MovimientoResponse> findByAccountNumber(String accountNumber, Pageable pageable) {
        cuentaRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));
        return movimientoRepository.findByAccountAccountNumber(accountNumber, pageable)
                .map(MovimientoResponse::from);
    }
}