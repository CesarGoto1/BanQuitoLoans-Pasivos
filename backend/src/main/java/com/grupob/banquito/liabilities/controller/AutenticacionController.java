package com.grupob.banquito.liabilities.controller;

import com.grupob.banquito.liabilities.dto.InicioSesionRequest;
import com.grupob.banquito.liabilities.dto.InicioSesionResponse;
import com.grupob.banquito.liabilities.service.AutenticacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    public AutenticacionController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    public ResponseEntity<InicioSesionResponse> login(@Valid @RequestBody InicioSesionRequest request) {
        return ResponseEntity.ok(autenticacionService.login(request.identificationNumber()));
    }
}
