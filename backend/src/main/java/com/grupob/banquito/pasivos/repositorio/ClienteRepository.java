package com.grupob.banquito.pasivos.repositorio;

import com.grupob.banquito.pasivos.entidad.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByCedulaCliente(String cedulaCliente);
}
