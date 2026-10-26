package com.grupob.banquito.pasivos.controlador;

import com.grupob.banquito.pasivos.dto.RespuestaCuenta;
import com.grupob.banquito.pasivos.dto.RespuestaCliente;
import com.grupob.banquito.pasivos.servicio.CuentaService;
import com.grupob.banquito.pasivos.servicio.ClienteService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteServicio;
    private final CuentaService cuentaServicio;

    public ClienteController(ClienteService clienteServicio, CuentaService cuentaServicio) {
        this.clienteServicio = clienteServicio;
        this.cuentaServicio = cuentaServicio;
    }

    @GetMapping(params = "idCliente")
    public ResponseEntity<RespuestaCliente> buscarClientePorId(@RequestParam("idCliente") Integer idCliente) {
        return ResponseEntity.ok(clienteServicio.buscarPorId(idCliente));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<RespuestaCliente> buscarCliente(@PathVariable("cedula") String cedulaCliente) {
        return ResponseEntity.ok(clienteServicio.buscarPorCedulaCliente(cedulaCliente));
    }

    @GetMapping("/{cedula}/cuentas")
    public ResponseEntity<List<RespuestaCuenta>> buscarCuentas(@PathVariable("cedula") String cedulaCliente) {
        return ResponseEntity.ok(cuentaServicio.buscarPorCedulaCliente(cedulaCliente));
    }
}
