import { NavLink, Navigate, Outlet, useNavigate } from 'react-router-dom';
import { borrarSesion, obtenerSesion } from '../servicios/sesion.js';

const URL_ACTIVOS = import.meta.env.VITE_URL_ACTIVOS ?? 'http://localhost:5173';

function urlGestionPrestamos(cliente) {
  const parametros = new URLSearchParams({
    idCliente: cliente.idCliente,
    cedulaCliente: cliente.cedulaCliente,
    nombresCliente: cliente.nombresCliente,
    apellidosCliente: cliente.apellidosCliente,
  });
  return `${URL_ACTIVOS}/banca-web?${parametros}`;
}

export default function MenuPagina() {
  const navegar = useNavigate();
  const cliente = obtenerSesion();

  if (!cliente) {
    return <Navigate to="/" replace />;
  }

  function cerrarSesion() {
    borrarSesion();
    navegar('/');
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
            {cliente.nombresCliente} {cliente.apellidosCliente}
          </span>
          <button type="button" className="bq-btn bq-btn-secundario bq-btn-sm" onClick={cerrarSesion}>
            Cerrar sesión
          </button>
        </div>
      </header>
      <nav className="bq-menu">
        <NavLink to="/banca/mi-cuenta" className="bq-menu-item">
          Mi cuenta
        </NavLink>
        <a href={urlGestionPrestamos(cliente)} className="bq-menu-item">
          Gestión de préstamos
        </a>
      </nav>
      <Outlet />
    </>
  );
}