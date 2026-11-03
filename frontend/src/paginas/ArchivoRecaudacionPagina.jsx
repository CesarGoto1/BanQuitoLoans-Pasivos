import { useState } from 'react';
import { generarArchivoRecaudacion } from '../servicios/pasivosApi.js';

const formateadorMoneda = new Intl.NumberFormat('es-EC', {
  style: 'currency',
  currency: 'USD',
});

const CANALES = {
  VEN: 'Ventanilla',
  WEB: 'Banca web',
  DEB: 'Débito automático',
  TRF: 'Transferencia',
};

function fechaDeHoy() {
  const hoy = new Date();
  const mes = String(hoy.getMonth() + 1).padStart(2, '0');
  const dia = String(hoy.getDate()).padStart(2, '0');
  return `${hoy.getFullYear()}-${mes}-${dia}`;
}

function formatearFecha(valor) {
  const [anio, mes, dia] = valor.split('-');
  return `${dia}/${mes}/${anio}`;
}

function formatearFechaHora(valor) {
  const [fecha, hora] = valor.split('T');
  return `${formatearFecha(fecha)} ${hora}`;
}

export default function ArchivoRecaudacionPagina() {
  const [fechaProceso, establecerFechaProceso] = useState(fechaDeHoy());
  const [archivo, establecerArchivo] = useState(null);
  const [error, establecerError] = useState('');
  const [generando, establecerGenerando] = useState(false);

  async function generar(evento) {
    evento.preventDefault();
    establecerError('');
    establecerArchivo(null);
    establecerGenerando(true);
    try {
      establecerArchivo(await generarArchivoRecaudacion(fechaProceso));
    } catch (excepcion) {
      establecerError(excepcion.message);
    } finally {
      establecerGenerando(false);
    }
  }

  function descargar() {
    const enlace = document.createElement('a');
    enlace.href = URL.createObjectURL(new Blob([archivo.contenido], { type: 'text/csv' }));
    enlace.download = archivo.nombreArchivo;
    enlace.click();
    URL.revokeObjectURL(enlace.href);
  }

  return (
    <main className="bq-pagina">
      <div className="bq-titulo-pagina">
        <div>
          <h1>Archivo de recaudación</h1>
          <p>Genera el archivo con los cobros de cuotas debitados de las cuentas en la fecha elegida</p>
        </div>
      </div>

      <form className="bq-tarjeta bq-formulario" onSubmit={generar}>
        <div className="bq-campo">
          <label className="bq-label" htmlFor="fecha-proceso">
            Fecha de los cobros
          </label>
          <input
            id="fecha-proceso"
            className="bq-input"
            type="date"
            required
            value={fechaProceso}
            onChange={(evento) => establecerFechaProceso(evento.target.value)}
          />
          <p className="bq-ayuda">
            Usa la misma fecha de negocio que vas a cerrar en el Core de Préstamos.
          </p>
        </div>
        <div className="bq-acciones">
          <button type="submit" className="bq-btn bq-btn-primario" disabled={generando}>
            {generando ? 'Generando…' : 'Generar archivo'}
          </button>
        </div>
      </form>

      {error && (
        <div className="bq-alerta bq-alerta-peligro" role="alert">
          <strong>No se pudo generar el archivo</strong>
          <span>{error}</span>
        </div>
      )}

      {archivo && (
        <>
          <div className="bq-alerta bq-alerta-exito" role="status">
            <strong>Archivo generado</strong>
            <span>
              {archivo.nombreArchivo} con {archivo.cobros.length}{' '}
              {archivo.cobros.length === 1 ? 'cobro' : 'cobros'} por{' '}
              {formateadorMoneda.format(archivo.montoTotal)}. Quedó guardado en la carpeta de recaudaciones del servidor.
            </span>
          </div>

          {archivo.cobros.length === 0 ? (
            <div className="bq-alerta bq-alerta-aviso">
              <strong>Sin cobros en esta fecha</strong>
              <span>El archivo se generó vacío. Verifica la fecha antes de cerrar el día.</span>
            </div>
          ) : (
            <div className="bq-tabla-contenedor">
              <table className="bq-tabla">
                <thead>
                  <tr>
                    <th>N.º</th>
                    <th>Operación</th>
                    <th>Cédula</th>
                    <th className="bq-num">Monto</th>
                    <th>Fecha y hora</th>
                    <th>Canal</th>
                    <th>Referencia</th>
                  </tr>
                </thead>
                <tbody>
                  {archivo.cobros.map((cobro) => (
                    <tr key={cobro.secuencial}>
                      <td>{cobro.secuencial}</td>
                      <td className="bq-mono">{cobro.numeroOperacion}</td>
                      <td className="bq-mono">{cobro.cedulaCliente}</td>
                      <td className="bq-num">{formateadorMoneda.format(cobro.monto)}</td>
                      <td>{formatearFechaHora(cobro.fechaHora)}</td>
                      <td>{CANALES[cobro.canal] ?? cobro.canal}</td>
                      <td>{cobro.referencia}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div className="bq-acciones">
            <button type="button" className="bq-btn bq-btn-secundario" onClick={descargar}>
              Descargar archivo
            </button>
          </div>
        </>
      )}
    </main>
  );
}
