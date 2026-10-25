package com.grupob.banquito.liabilities.repository;

import com.grupob.banquito.liabilities.entity.Cuenta;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

    List<Cuenta> findByCustomerIdentificationNumber(String identificationNumber);

    Optional<Cuenta> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cuenta> findForUpdateByAccountNumber(String accountNumber);
}
