const URL_BASE = import.meta.env.VITE_API_PASIVOS;

async function solicitar(ruta, opciones) {
  let respuesta;
  try {
    respuesta = await fetch(`${URL_BASE}${ruta}`, opciones);
  } catch {
    throw new Error('No se pudo conectar con el servidor.');
  }

  if (!respuesta.ok) {
    let detalle = 'Ocurrió un error inesperado.';
    try {
      const cuerpo = await respuesta.json();
      if (cuerpo.detail) {
        detalle = cuerpo.detail;
      }
    } catch {
      detalle = 'Ocurrió un error inesperado.';
    }
    throw new Error(detalle);
  }

  return respuesta.json();
}

export function iniciarSesion(cedulaCliente) {
  return solicitar('/api/autenticacion/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ cedulaCliente }),
  });
}

export function obtenerCuentas(cedulaCliente) {
  return solicitar(`/api/clientes/${encodeURIComponent(cedulaCliente)}/cuentas`);
}

export function obtenerMovimientos(numeroCuenta, pagina = 0, tamano = 10) {
  return solicitar(
    `/api/cuentas/${encodeURIComponent(numeroCuenta)}/movimientos?page=${pagina}&size=${tamano}`
  );
}