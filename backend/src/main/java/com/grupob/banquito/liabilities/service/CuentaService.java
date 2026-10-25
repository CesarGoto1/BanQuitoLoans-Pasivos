package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.CuentaResponse;
import java.util.List;

public interface CuentaService {

    List<CuentaResponse> findByCustomerIdentificationNumber(String identificationNumber);
}
