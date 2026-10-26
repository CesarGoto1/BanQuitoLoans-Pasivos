package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.RespuestaMovimiento;
import com.grupob.banquito.pasivos.servicio.MovimientoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class MovimientoController {

    private final MovimientoService movimientoServicio;

    public MovimientoController(MovimientoService movimientoServicio) {
        this.movimientoServicio = movimientoServicio;
    }

    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<Page<RespuestaMovimiento>> buscarMovimientos(
            @PathVariable("numeroCuenta") String numeroCuenta,
            @PageableDefault(sort = {"fechaMovimiento", "idMovimiento"}, direction = Sort.Direction.DESC) Pageable paginacion) {
        return ResponseEntity.ok(movimientoServicio.buscarPorNumeroCuenta(numeroCuenta, paginacion));
    }
}
