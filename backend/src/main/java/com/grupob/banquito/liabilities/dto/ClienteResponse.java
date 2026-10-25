package com.grupob.banquito.liabilities.dto;

import com.grupob.banquito.liabilities.entity.Cliente;
import com.grupob.banquito.liabilities.entity.EstadoCliente;
import java.time.LocalDate;

public record ClienteResponse(
        String identificationNumber,
        String firstNames,
        String lastNames,
        LocalDate birthDate,
        String address,
        String city,
        String phone,
        String email,
        EstadoCliente status) {

    public static ClienteResponse from(Cliente customer) {
        return new ClienteResponse(
                customer.getIdentificationNumber(),
                customer.getFirstNames(),
                customer.getLastNames(),
                customer.getBirthDate(),
                customer.getAddress(),
                customer.getCity(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getStatus());
    }
}
