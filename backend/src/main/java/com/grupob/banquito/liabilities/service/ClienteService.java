package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.ClienteResponse;

public interface ClienteService {

    ClienteResponse findByIdentificationNumber(String identificationNumber);
}
