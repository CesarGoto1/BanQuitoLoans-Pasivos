package com.grupob.banquito.liabilities.controller;

import com.grupob.banquito.liabilities.dto.MovimientoResponse;
import com.grupob.banquito.liabilities.service.MovimientoService;
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
@RequestMapping("/api/accounts")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping("/{accountNumber}/movements")
    public ResponseEntity<Page<MovimientoResponse>> findMovements(
            @PathVariable String accountNumber,
            @PageableDefault(sort = {"movementDate", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movimientoService.findByAccountNumber(accountNumber, pageable));
    }
}
