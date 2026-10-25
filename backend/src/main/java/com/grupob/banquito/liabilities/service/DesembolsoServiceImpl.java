package com.grupob.banquito.liabilities.service;

import com.grupob.banquito.liabilities.dto.DesembolsoRequest;
import com.grupob.banquito.liabilities.dto.DesembolsoResponse;
import com.grupob.banquito.liabilities.dto.DesembolsoResultado;
import com.grupob.banquito.liabilities.entity.Cuenta;
import com.grupob.banquito.liabilities.entity.EstadoCliente;
import com.grupob.banquito.liabilities.entity.EstadoCuenta;
import com.grupob.banquito.liabilities.entity.Movimiento;
import com.grupob.banquito.liabilities.repository.CuentaRepository;
import com.grupob.banquito.liabilities.repository.MovimientoRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DesembolsoServiceImpl implements DesembolsoService {

    private static final String VERSION = "1.0";
    private static final String CONCEPT = "DESEMBOLSO";
    private static final String TYPE = "CREDITO";
    private static final String AUDIT_USER = "core-activos";

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public DesembolsoServiceImpl(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional
    public DesembolsoResultado credit(DesembolsoRequest request) {
        if (!VERSION.equals(request.version())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Versión de instrucción no soportada: " + request.version() + ".");
        }
        if (request.approvedAmount().subtract(request.solcaContribution()).compareTo(request.netAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El monto neto debe ser el monto aprobado menos la contribución SOLCA.");
        }

        Cuenta account = cuentaRepository.findForUpdateByAccountNumber(request.destinationAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));

        Movimiento existing = movimientoRepository
                .findFirstByOperationNumberAndConcept(request.loanNumber(), CONCEPT).orElse(null);
        if (existing != null) {
            if (!sameInstruction(existing, request)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "La operación " + request.loanNumber() + " ya fue acreditada con otros datos.");
            }
            return new DesembolsoResultado(DesembolsoResponse.from(existing), false);
        }

        if (!account.getCustomer().getIdentificationNumber().equals(request.customerNationalId())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La cuenta no pertenece al cliente indicado.");
        }
        if (account.getStatus() != EstadoCuenta.ACTIVA || account.getCustomer().getStatus() != EstadoCliente.ACTIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "La cuenta o el cliente no están activos.");
        }

        LocalDateTime now = LocalDateTime.now();
        account.setBalance(account.getBalance().add(request.netAmount()));
        account.setLastModifiedBy(AUDIT_USER);
        account.setLastModifiedDate(now);

        Movimiento movement = new Movimiento();
        movement.setAccount(account);
        movement.setType(TYPE);
        movement.setConcept(CONCEPT);
        movement.setAmount(request.netAmount());
        movement.setBalanceAfter(account.getBalance());
        movement.setMovementDate(request.disbursementDate());
        movement.setMovementDateTime(now);
        movement.setTransactionUuid(UUID.randomUUID());
        movement.setOperationNumber(request.loanNumber());
        movement.setReference(request.reference());
        movement.setCreatedBy(AUDIT_USER);
        movement.setCreatedDate(now);
        movimientoRepository.save(movement);

        return new DesembolsoResultado(DesembolsoResponse.from(movement), true);
    }

    private boolean sameInstruction(Movimiento existing, DesembolsoRequest request) {
        return existing.getAccount().getAccountNumber().equals(request.destinationAccount())
                && existing.getAmount().compareTo(request.netAmount()) == 0;
    }
}
