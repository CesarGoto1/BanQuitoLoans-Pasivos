package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.MovimientoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovimientoService {

    Page<MovimientoResponse> findByAccountNumber(String accountNumber, Pageable pageable);
}
