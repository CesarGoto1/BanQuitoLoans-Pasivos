package com.grupob.banquito.liabilities.controller;

import com.grupob.banquito.liabilities.dto.DesembolsoRequest;
import com.grupob.banquito.liabilities.dto.DesembolsoResponse;
import com.grupob.banquito.liabilities.dto.DesembolsoResultado;
import com.grupob.banquito.liabilities.service.DesembolsoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disbursements")
public class DesembolsoController {

    private final DesembolsoService desembolsoService;

    public DesembolsoController(DesembolsoService desembolsoService) {
        this.desembolsoService = desembolsoService;
    }

    @PostMapping
    public ResponseEntity<DesembolsoResponse> credit(@Valid @RequestBody DesembolsoRequest request) {
        DesembolsoResultado result = desembolsoService.credit(request);
        return ResponseEntity.status(result.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(result.disbursement());
    }
}
