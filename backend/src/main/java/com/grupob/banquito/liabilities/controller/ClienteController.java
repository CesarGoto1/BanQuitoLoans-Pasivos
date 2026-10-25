package com.grupob.banquito.liabilities.controller;

import com.grupob.banquito.liabilities.dto.CuentaResponse;
import com.grupob.banquito.liabilities.dto.ClienteResponse;
import com.grupob.banquito.liabilities.service.CuentaService;
import com.grupob.banquito.liabilities.service.ClienteService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class ClienteController {

    private final ClienteService clienteService;
    private final CuentaService cuentaService;

    public ClienteController(ClienteService clienteService, CuentaService cuentaService) {
        this.clienteService = clienteService;
        this.cuentaService = cuentaService;
    }

    @GetMapping(params = "customerId")
    public ResponseEntity<ClienteResponse> findCustomerById(@RequestParam Integer customerId) {
        return ResponseEntity.ok(clienteService.findById(customerId));
    }

    @GetMapping("/{identificationNumber}")
    public ResponseEntity<ClienteResponse> findCustomer(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(clienteService.findByIdentificationNumber(identificationNumber));
    }

    @GetMapping("/{identificationNumber}/accounts")
    public ResponseEntity<List<CuentaResponse>> findAccounts(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(cuentaService.findByCustomerIdentificationNumber(identificationNumber));
    }
}
