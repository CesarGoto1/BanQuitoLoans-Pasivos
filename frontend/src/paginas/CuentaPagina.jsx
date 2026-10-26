import { useEffect, useState } from 'react';
import { obtenerCuentas, obtenerMovimientos } from '../servicios/pasivosApi.js';
import { obtenerSesion } from '../servicios/sesion.js';

const TAMANO_PAGINA = 10;

const formateadorMoneda = new Intl.NumberFormat('es-EC', {
  style: 'currency',
  currency: 'USD',
});

function formatearMoneda(valor) {
  return formateadorMoneda.format(valor);
}

function formatearFecha(valor) {
  if (!valor) {
    return '';
  }
  const [anio, mes, dia] = valor.substring(0, 10).split('-');
  return `${dia}/${mes}/${anio}`;
}

const CLASE_ESTADO = {
  ACTIVA: 'bq-estado-exito',
  INACTIVA: 'bq-estado-aviso',
  CERRADA: 'bq-estado-peligro',
};

export default function CuentaPagina() {
  const cedulaCliente = obtenerSesion().cedulaCliente;
  const [cuentas, establecerCuentas] = useState([]);
  const [numeroSeleccionado, establecerNumeroSeleccionado] = useState('');
  const [movimientos, establecerMovimientos] = useState(null);
  const [pagina, establecerPagina] = useState(0);
  const [error, establecerError] = useState('');
  const [cargando, establecerCargando] = useState(true);

  useEffect(() => {
    obtenerCuentas(cedulaCliente)
      .then((datos) => {
        establecerCuentas(datos);
        if (datos.length > 0) {
          establecerNumeroSeleccionado(datos[0].numeroCuenta);
        }
      })
      .catch((excepcion) => establecerError(excepcion.message))
      .finally(() => establecerCargando(false));
  }, [cedulaCliente]);

  useEffect(() => {
    if (!numeroSeleccionado) {
      return;
    }
    establecerError('');
    obtenerMovimientos(numeroSeleccionado, pagina, TAMANO_PAGINA)
      .then(establecerMovimientos)
      .catch((excepcion) => establecerError(excepcion.message));
  }, [numeroSeleccionado, pagina]);

  function seleccionarCuenta(numeroCuenta) {
    establecerMovimientos(null);
    establecerPagina(0);
    establecerNumeroSeleccionado(numeroCuenta);
  }

  const cuenta = cuentas.find((elemento) => elemento.numeroCuenta === numeroSeleccionado);

  const informacionPagina = movimientos ? movimientos.page ?? movimientos : null;
  const paginaActual = informacionPagina?.number ?? pagina;
  const totalPaginas = informacionPagina?.totalPages ?? 1;

  return (
    <main className="bq-pagina">
      <div className="bq-titulo-pagina">
        <div>
          <h1>Mi cuenta</h1>
          <p>Saldo y movimientos de tus cuentas</p>
        </div>
      </div>

      {error && (
        <div className="bq-alerta bq-alerta-peligro" role="alert">
          <strong>Ocurrió un problema</strong>
          <span>{error}</span>
        </div>
      )}

      {cargando && <p className="bq-texto-2">Cargando...</p>}

      {!cargando && !error && cuentas.length === 0 && (
        <div className="bq-alerta bq-alerta-info">
          <strong>Sin cuentas</strong>
          <span>No tienes cuentas registradas.</span>
        </div>
      )}

      {cuentas.length > 1 && (
        <div className="bq-segmentado">
          {cuentas.map((elemento) => (
            <button
              key={elemento.numeroCuenta}
              type="button"
              aria-pressed={elemento.numeroCuenta === numeroSeleccionado}
              onClick={() => seleccionarCuenta(elemento.numeroCuenta)}
            >
              {elemento.numeroCuenta}
            </button>
          ))}
        </div>
      )}

      {cuenta && (
        <div className="bq-fila">
          <div className="bq-tarjeta-destacada">
            <span className="bq-etiqueta-seccion">Saldo</span>
            <span className="bq-monto-grande">{formatearMoneda(cuenta.saldoCuenta)}</span>
          </div>
          <div className="bq-tarjeta">
            <div className="bq-datos">
              <div className="bq-dato">
                <span>Número de cuenta</span>
                <span>{cuenta.numeroCuenta}</span>
              </div>
              <div className="bq-dato">
                <span>Tipo</span>
                <span>{cuenta.tipo}</span>
              </div>
              <div className="bq-dato">
                <span>Fecha de apertura</span>
                <span>{formatearFecha(cuenta.fechaApertura)}</span>
              </div>
              <div className="bq-dato">
                <span>Estado</span>
                <span className={`bq-estado ${CLASE_ESTADO[cuenta.estado] || 'bq-estado-neutro'}`}>
                  {cuenta.estado}
                </span>
              </div>
            </div>
          </div>
        </div>
      )}

      {cuenta && <h2>Movimientos</h2>}

      {movimientos && movimientos.content.length === 0 && (
        <div className="bq-alerta bq-alerta-info">
          <strong>Sin movimientos</strong>
          <span>Esta cuenta aún no tiene movimientos.</span>
        </div>
      )}

      {movimientos && movimientos.content.length > 0 && (
        <>
          <div className="bq-tabla-contenedor">
            <table className="bq-tabla">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Concepto</th>
                  <th>Tipo</th>
                  <th className="bq-num">Monto</th>
                  <th className="bq-num">Saldo posterior</th>
                  <th>Operación</th>
                  <th>Canal</th>
                </tr>
              </thead>
              <tbody>
                {movimientos.content.map((movimiento) => (
                  <tr key={movimiento.uuidTransaccion}>
                    <td>{formatearFecha(movimiento.fechaMovimiento)}</td>
                    <td>{movimiento.conceptoMovimiento}</td>
                    <td>{movimiento.tipo}</td>
                    <td className="bq-num">{formatearMoneda(movimiento.montoMovimiento)}</td>
                    <td className="bq-num">{formatearMoneda(movimiento.saldoPosterior)}</td>
                    <td>{movimiento.numeroOperacion}</td>
                    <td>{movimiento.canal}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="bq-acciones">
            <span className="bq-texto-2">
              Página {paginaActual + 1} de {totalPaginas}
            </span>
            <button
              type="button"
              className="bq-btn bq-btn-secundario bq-btn-sm"
              disabled={paginaActual <= 0}
              onClick={() => establecerPagina(pagina - 1)}
            >
              Anterior
            </button>
            <button
              type="button"
              className="bq-btn bq-btn-secundario bq-btn-sm"
              disabled={paginaActual + 1 >= totalPaginas}
              onClick={() => establecerPagina(pagina + 1)}
            >
              Siguiente
            </button>
          </div>
        </>
      )}
    </main>
  );
}