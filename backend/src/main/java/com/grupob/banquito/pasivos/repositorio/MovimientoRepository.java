package com.grupob.banquito.pasivos.repositorio;

import com.grupob.banquito.pasivos.entidad.Movimiento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    Page<Movimiento> findByCuentaNumeroCuenta(String numeroCuenta, Pageable paginacion);

    Optional<Movimiento> findFirstByNumeroOperacionAndConceptoMovimiento(String numeroOperacion, String conceptoMovimiento);
}
