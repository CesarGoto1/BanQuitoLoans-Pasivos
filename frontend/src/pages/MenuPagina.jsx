import { NavLink, Navigate, Outlet, useNavigate } from 'react-router-dom';
import { clearSession, getSession } from '../utils/sesion.js';

export default function MenuPagina() {
  const navigate = useNavigate();
  const customer = getSession();

  if (!customer) {
    return <Navigate to="/" replace />;
  }

  function handleLogout() {
    clearSession();
    navigate('/');
  }

  return (
    <>
      <header className="bq-cabecera">
        <div className="bq-marca">
          <img className="bq-logo" src="/logo-bq.png" alt="BanQuito" />
          <span className="bq-marca-nombre">BanQuito</span>
          <span className="bq-marca-app">Banca Web</span>
        </div>
        <div className="bq-cabecera-usuario">
          <span>
            {customer.firstNames} {customer.lastNames}
          </span>
          <button type="button" className="bq-btn bq-btn-secundario bq-btn-sm" onClick={handleLogout}>
            Cerrar sesión
          </button>
        </div>
      </header>
      <nav className="bq-menu">
        <NavLink to="/banca/mi-cuenta" className="bq-menu-item">
          Mi cuenta
        </NavLink>
      </nav>
      <Outlet />
    </>
  );
}