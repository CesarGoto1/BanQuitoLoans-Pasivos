import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { iniciarSesion } from '../servicios/pasivosApi.js';
import { guardarSesion } from '../servicios/sesion.js';

export default function InicioSesionPagina() {
  const [cedulaCliente, establecerCedulaCliente] = useState('');
  const [error, establecerError] = useState('');
  const [cargando, establecerCargando] = useState(false);
  const navegar = useNavigate();

  function manejarCambio(evento) {
    establecerCedulaCliente(evento.target.value.replace(/\D/g, ''));
  }

  async function enviarFormulario(evento) {
    evento.preventDefault();
    establecerError('');
    establecerCargando(true);
    try {
      const cliente = await iniciarSesion(cedulaCliente);
      guardarSesion(cliente);
      navegar('/banca');
    } catch (excepcion) {
      establecerError(excepcion.message);
    } finally {
      establecerCargando(false);
    }
  }

  return (
    <main className="bq-ingreso">
      <section className="bq-ingreso-panel">
        <div className="bq-marca">
          <img className="bq-logo" src="/logo-bq.png" alt="BanQuito" />
          <span className="bq-marca-nombre">BanQuito</span>
        </div>
        <div className="bq-formulario">
          <h1>Banca Web</h1>
          <p>Consulta tus cuentas y movimientos desde cualquier lugar.</p>
        </div>
        <ul className="bq-ingreso-lista">
          <li>Saldo de tus cuentas</li>
          <li>Historial de movimientos</li>
          <li>Acceso con tu cédula</li>
        </ul>
      </section>
      <section className="bq-ingreso-formulario">
        <form className="bq-formulario" onSubmit={enviarFormulario}>
          <h2>Ingresa a tu banca</h2>
          <p className="bq-texto-2">Escribe tu número de cédula para continuar.</p>
          {error && (
            <div className="bq-alerta bq-alerta-peligro" role="alert">
              <strong>No se pudo ingresar</strong>
              <span>{error}</span>
            </div>
          )}
          <div className="bq-campo">
            <label className="bq-label" htmlFor="cedulaCliente">
              Cédula <span className="bq-obligatorio">*</span>
            </label>
            <input
              id="cedulaCliente"
              className="bq-input"
              type="text"
              inputMode="numeric"
              maxLength={10}
              placeholder="10 dígitos"
              value={cedulaCliente}
              onChange={manejarCambio}
              autoFocus
            />
          </div>
          <button
            type="submit"
            className="bq-btn bq-btn-primario bq-btn-bloque"
            disabled={cargando || cedulaCliente.length !== 10}
          >
            {cargando ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>
      </section>
    </main>
  );
}