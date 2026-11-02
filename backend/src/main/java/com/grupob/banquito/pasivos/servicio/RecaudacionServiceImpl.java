package com.grupob.banquito.pasivos.servicio;

import com.grupob.banquito.pasivos.dto.ArchivoRecaudacion;
import com.grupob.banquito.pasivos.entidad.Movimiento;
import com.grupob.banquito.pasivos.repositorio.MovimientoRepository;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HexFormat;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

@Service
public class RecaudacionServiceImpl implements RecaudacionService {

    private static final String COLLECTION = "COBRO_CUOTA";
    private static final Map<String, String> CHANNEL_CODES = Map.of(
            "VENTANILLA", "VEN",
            "BANCA_WEB", "WEB",
            "DEBITO_AUTOMATICO", "DEB",
            "TRANSFERENCIA", "TRF");
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final MovimientoRepository movimientoRepository;
    private final Path outputDirectory;

    public RecaudacionServiceImpl(MovimientoRepository movimientoRepository,
                                  @Value("${recaudacion.output-dir:./recaudaciones}") String outputDirectory) {
        this.movimientoRepository = movimientoRepository;
        this.outputDirectory = Path.of(outputDirectory);
    }

    @Override
    @Transactional(readOnly = true)
    public ArchivoRecaudacion generar(LocalDate fechaProceso) {
        List<Movimiento> movimientos = movimientoRepository
                .findByFechaMovimientoAndConceptoMovimientoOrderByIdMovimiento(fechaProceso, COLLECTION);
        String base = "REC_" + FILE_DATE.format(fechaProceso);
        int sequence = nextSequence(base);
        String idArchivo = base + "_" + String.format("%03d", sequence);
        StringBuilder detalles = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;
        int secuencial = 1;
        for (Movimiento movimiento : movimientos) {
            if (movimiento.getNumeroOperacion() == null || movimiento.getCuenta() == null
                    || movimiento.getCuenta().getCliente() == null) {
                throw new IllegalStateException("El cobro " + movimiento.getIdMovimiento()
                        + " no tiene operación o cliente titular.");
            }
            String canal = movimiento.getCanal();
            String codigoCanal = CHANNEL_CODES.get(canal);
            if (codigoCanal == null) {
                throw new IllegalStateException("El cobro " + movimiento.getIdMovimiento() + " tiene un canal inválido.");
            }
            String detalle = String.join(",",
                    "D", Integer.toString(secuencial++), movimiento.getUuidTransaccion().toString(),
                    csv(movimiento.getNumeroOperacion()), movimiento.getCuenta().getCliente().getCedulaCliente(),
                    money(movimiento.getMontoMovimiento()), DATE_TIME.format(movimiento.getFechaHoraMovimiento()),
                    codigoCanal, csv(movimiento.getReferencia()));
            if (detalles.length() > 0) {
                detalles.append('\n');
            }
            detalles.append(detalle);
            total = total.add(movimiento.getMontoMovimiento());
        }
        String detailText = detalles.toString();
        String hash = sha256(detailText);
        String header = String.join(",", "H", idArchivo, fechaProceso.toString(),
                DATE_TIME.format(LocalDateTime.now().withNano(0)), Integer.toString(movimientos.size()), money(total));
        String footer = String.join(",", "T", Integer.toString(movimientos.size()), money(total), hash);
        String content = header + (detailText.isEmpty() ? "" : "\n" + detailText) + "\n" + footer + "\n";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        try {
            Files.createDirectories(outputDirectory);
            Files.write(outputDirectory.resolve(idArchivo + ".csv"), bytes);
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo escribir el archivo " + idArchivo + ".", exception);
        }
        return new ArchivoRecaudacion(idArchivo + ".csv", bytes);
    }

    private int nextSequence(String base) {
        try {
            Files.createDirectories(outputDirectory);
            for (int sequence = 1; sequence <= 999; sequence++) {
                String name = base + "_" + String.format("%03d", sequence) + ".csv";
                if (!Files.exists(outputDirectory.resolve(name))) {
                    return sequence;
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo preparar la carpeta de recaudaciones.", exception);
        }
        throw new IllegalStateException("Se agotaron los secuenciales de recaudación para " + base + ".");
    }

    private String money(BigDecimal value) {
        return value.setScale(2).toPlainString();
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        return escaped.indexOf(',') >= 0 || escaped.indexOf('"') >= 0
                || escaped.indexOf('\n') >= 0 ? "\"" + escaped + "\"" : escaped;
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no está disponible en la JVM.", exception);
        }
    }
}
