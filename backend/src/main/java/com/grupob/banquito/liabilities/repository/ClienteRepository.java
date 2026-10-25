package com.grupob.banquito.liabilities.repository;

import com.grupob.banquito.liabilities.entity.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByIdentificationNumber(String identificationNumber);
}
