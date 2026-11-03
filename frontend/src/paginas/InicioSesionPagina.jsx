import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { iniciarSesion } from '../servicios/pasivosApi.js';
import { guardarSesion } from '../servicios/sesion.js';

const URL_ACTIVOS = import.meta.env.VITE_URL_ACTIVOS ?? 'http://localhost:5173';

const BENEFICIOS = [
  {
    titulo: 'Tus cuentas',
    detalle: 'Saldo disponible al instante.',
    icono: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M20 7H5a2 2 0 0 1 0-4h13v4" />
        <path d="M3 5v14a2 2 0 0 0 2 2h15V7" />
        <path d="M16 14h.01" />
      </svg>
    ),
  },
  {
    titulo: 'Movimientos',
    detalle: 'Historial de cada depósito y retiro.',
    icono: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M7 4v16M3 8l4-4 4 4" />
        <path d="M17 20V4M13 16l4 4 4-4" />
      </svg>
    ),
  },
  {
    titulo: 'Préstamos',
    detalle: 'Simula y solicita tu crédito en línea.',
    icono: (
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M3 3v18h18" />
        <path d="M8 17v-4M12 17V9M16 17V6" />
      </svg>
    ),
  },
];

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
        <div className="bq-ingreso-mensaje">
          <span className="bq-ingreso-etiqueta">Banca Web</span>
          <h1>
            Tu dinero y tu crédito, <em>en un solo lugar.</em>
          </h1>
          <p className="bq-ingreso-descripcion">
            Consulta tus cuentas, revisa tus movimientos y gestiona tus préstamos desde cualquier lugar.
          </p>
          <ul className="bq-ingreso-lista">
            {BENEFICIOS.map((beneficio) => (
              <li key={beneficio.titulo}>
                <span className="bq-ingreso-icono" aria-hidden="true">{beneficio.icono}</span>
                <span>
                  <strong>{beneficio.titulo}</strong>
                  <small>{beneficio.detalle}</small>
                </span>
              </li>
            ))}
          </ul>
        </div>
        <div className="bq-ingreso-pie">
          <span className="bq-ingreso-lema">
            <span className="bq-ingreso-cuotas" aria-hidden="true">
              <span />
              <span />
              <span />
              <span />
            </span>
            Tu crédito, cuota a cuota.
          </span>
          <span className="bq-ingreso-seguro">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <rect x="4" y="11" width="16" height="10" rx="2" />
              <path d="M8 11V7a4 4 0 0 1 8 0v4" />
            </svg>
            Conexión segura
          </span>
        </div>
      </section>
      <section className="bq-ingreso-formulario">
        <form className="bq-formulario bq-ingreso-tarjeta" onSubmit={enviarFormulario}>
          <div className="bq-ingreso-encabezado">
            <span className="bq-ingreso-icono" aria-hidden="true">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 10 12 4l9 6" />
                <path d="M5 10v8M9.5 10v8M14.5 10v8M19 10v8M3 21h18" />
              </svg>
            </span>
            <h2>Ingresa a tu banca</h2>
            <p className="bq-texto-2">Usa tu número de cédula para continuar.</p>
          </div>
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
            <div className="bq-ingreso-entrada">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                <rect x="2" y="5" width="20" height="14" rx="2" />
                <circle cx="8" cy="11" r="2" />
                <path d="M5 16a3 3 0 0 1 6 0M14 10h4M14 14h3" />
              </svg>
              <input
                id="cedulaCliente"
                className="bq-input"
                type="text"
                inputMode="numeric"
                autoComplete="username"
                maxLength={10}
                placeholder="Ej. 1712345678"
                aria-describedby="cedulaClienteAyuda"
                value={cedulaCliente}
                onChange={manejarCambio}
                autoFocus
              />
            </div>
            <div className="bq-ingreso-ayuda" id="cedulaClienteAyuda">
              <span>10 dígitos, sin guiones</span>
              <span className="bq-ingreso-contador" data-completo={cedulaCliente.length === 10}>
                {cedulaCliente.length}/10
              </span>
            </div>
          </div>
          <button
            type="submit"
            className="bq-btn bq-btn-primario bq-btn-bloque bq-ingreso-boton"
            disabled={cargando || cedulaCliente.length !== 10}
          >
            {cargando ? 'Ingresando...' : 'Ingresar'}
            {!cargando && (
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                <path d="M5 12h14M13 6l6 6-6 6" />
              </svg>
            )}
          </button>
          <div className="bq-ingreso-aviso">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
              <path d="m9 12 2 2 4-4" />
            </svg>
            <span>BanQuito nunca te pedirá tus datos por correo, llamada o mensaje.</span>
          </div>
        </form>
        <a className="bq-ingreso-volver" href={URL_ACTIVOS}>
          ← Volver a la página principal
        </a>
        <p className="bq-ingreso-ayuda-pie">¿Necesitas ayuda? Acércate a cualquier agencia BanQuito.</p>
      </section>
    </main>
  );
}
