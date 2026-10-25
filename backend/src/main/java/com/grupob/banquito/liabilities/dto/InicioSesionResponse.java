package com.grupob.banquito.liabilities.dto;

import com.grupob.banquito.liabilities.entity.Cliente;
import com.grupob.banquito.liabilities.entity.EstadoCliente;

public record InicioSesionResponse(
        String identificationNumber,
        String firstNames,
        String lastNames,
        EstadoCliente status) {

    public static InicioSesionResponse from(Cliente customer) {
        return new InicioSesionResponse(
                customer.getIdentificationNumber(),
                customer.getFirstNames(),
                customer.getLastNames(),
                customer.getStatus());
    }
}
