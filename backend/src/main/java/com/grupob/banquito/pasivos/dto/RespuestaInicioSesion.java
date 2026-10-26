package com.grupob.banquito.pasivos.dto;
import com.grupob.banquito.pasivos.entidad.Cliente;
import com.grupob.banquito.pasivos.entidad.EstadoCliente;
public record RespuestaInicioSesion(
        Integer idCliente,
        String cedulaCliente,
        String nombresCliente,
        String apellidosCliente,
        EstadoCliente estado) {
    public static RespuestaInicioSesion desde(Cliente cliente) {
        return new RespuestaInicioSesion(
                cliente.getIdCliente(),
                cliente.getCedulaCliente(),
                cliente.getNombresCliente(),
                cliente.getApellidosCliente(),
                cliente.getEstadoCliente());
    }
}
