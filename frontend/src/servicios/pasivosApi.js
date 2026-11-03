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

async function leerError(respuesta) {
  try {
    const cuerpo = await respuesta.json();
    if (cuerpo.detail) {
      return cuerpo.detail;
    }
  } catch {
    return 'Ocurrió un error inesperado.';
  }
  return 'Ocurrió un error inesperado.';
}

function interpretarArchivoRecaudacion(contenido) {
  const lineas = contenido.split('\n').filter((linea) => linea.length > 0);
  const encabezado = lineas.find((linea) => linea.startsWith('H,')).split(',');
  const cobros = lineas
    .filter((linea) => linea.startsWith('D,'))
    .map((linea) => {
      const campos = linea.split(',');
      return {
        secuencial: campos[1],
        numeroOperacion: campos[3],
        cedulaCliente: campos[4],
        monto: Number(campos[5]),
        fechaHora: campos[6],
        canal: campos[7],
        referencia: campos[8] ?? '',
      };
    });
  return {
    nombreArchivo: `${encabezado[1]}.csv`,
    contenido,
    cobros,
    montoTotal: Number(encabezado[5]),
  };
}

export async function generarArchivoRecaudacion(fechaProceso) {
  let respuesta;
  try {
    respuesta = await fetch(
      `${URL_BASE}/api/recaudaciones/archivo?fechaProceso=${encodeURIComponent(fechaProceso)}`
    );
  } catch {
    throw new Error('No se pudo conectar con el servidor.');
  }
  if (!respuesta.ok) {
    throw new Error(await leerError(respuesta));
  }
  return interpretarArchivoRecaudacion(await respuesta.text());
}
