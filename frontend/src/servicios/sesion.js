const CLAVE_SESION = 'bq-session';

export function guardarSesion(cliente) {
  sessionStorage.setItem(CLAVE_SESION, JSON.stringify(cliente));
}

export function obtenerSesion() {
  const contenido = sessionStorage.getItem(CLAVE_SESION);
  return contenido ? JSON.parse(contenido) : null;
}

export function borrarSesion() {
  sessionStorage.removeItem(CLAVE_SESION);
}