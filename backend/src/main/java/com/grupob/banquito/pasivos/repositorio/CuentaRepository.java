package com.grupob.banquito.pasivos.repositorio;

import com.grupob.banquito.pasivos.entidad.Cuenta;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {

    List<Cuenta> findByClienteCedulaCliente(String cedulaCliente);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cuenta> findParaActualizarByNumeroCuenta(String numeroCuenta);
}
