package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.InicioSesionResponse;

public interface AutenticacionService {

    InicioSesionResponse login(String identificationNumber);
}
