package com.grupob.banquito.pasivos.dto;
import com.grupob.banquito.pasivos.entidad.Cliente;
import com.grupob.banquito.pasivos.entidad.EstadoCliente;
import java.time.LocalDate;
public record RespuestaCliente(
        Integer idCliente,
        String cedulaCliente,
        String nombresCliente,
        String apellidosCliente,
        LocalDate fechaNacimiento,
        String direccionCliente,
        String ciudadCliente,
        String telefonoCliente,
        String correoCliente,
        EstadoCliente estado) {
    public static RespuestaCliente desde(Cliente cliente) {
        return new RespuestaCliente(
                cliente.getIdCliente(),
                cliente.getCedulaCliente(),
                cliente.getNombresCliente(),
                cliente.getApellidosCliente(),
                cliente.getFechaNacimiento(),
                cliente.getDireccionCliente(),
                cliente.getCiudadCliente(),
                cliente.getTelefonoCliente(),
                cliente.getCorreoCliente(),
                cliente.getEstadoCliente());
    }
}
