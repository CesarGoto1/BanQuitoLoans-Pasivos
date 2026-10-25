package com.grupob.banquito.liabilities.repository;

import com.grupob.banquito.liabilities.entity.Cuenta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

    List<Cuenta> findByCustomerIdentificationNumber(String identificationNumber);

    Optional<Cuenta> findByAccountNumber(String accountNumber);
}
