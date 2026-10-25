import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/pasivosApi.js';
import { saveSession } from '../utils/sesion.js';

export default function LoginPagina() {
  const [identificationNumber, setIdentificationNumber] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  function handleChange(event) {
    setIdentificationNumber(event.target.value.replace(/\D/g, ''));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    setLoading(true);
    try {
      const customer = await login(identificationNumber);
      saveSession(customer);
      navigate('/banca');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
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
        <form className="bq-formulario" onSubmit={handleSubmit}>
          <h2>Ingresa a tu banca</h2>
          <p className="bq-texto-2">Escribe tu número de cédula para continuar.</p>
          {error && (
            <div className="bq-alerta bq-alerta-peligro" role="alert">
              <strong>No se pudo ingresar</strong>
              <span>{error}</span>
            </div>
          )}
          <div className="bq-campo">
            <label className="bq-label" htmlFor="identificationNumber">
              Cédula <span className="bq-obligatorio">*</span>
            </label>
            <input
              id="identificationNumber"
              className="bq-input"
              type="text"
              inputMode="numeric"
              maxLength={10}
              placeholder="10 dígitos"
              value={identificationNumber}
              onChange={handleChange}
              autoFocus
            />
          </div>
          <button
            type="submit"
            className="bq-btn bq-btn-primario bq-btn-bloque"
            disabled={loading || identificationNumber.length !== 10}
          >
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>
      </section>
    </main>
  );
}