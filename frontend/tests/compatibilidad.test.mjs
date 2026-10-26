import { test } from 'node:test';
import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';

async function cargarModulo(ruta, sustituciones = []) {
  let codigo = await readFile(new URL(ruta, import.meta.url), 'utf8');
  for (const [anterior, nuevo] of sustituciones) codigo = codigo.replaceAll(anterior, nuevo);
  return import(`data:text/javascript;base64,${Buffer.from(codigo).toString('base64')}`);
}

test('API conserva rutas, datos de inicio de sesion, paginacion y errores', async () => {
  const api = await cargarModulo('../src/servicios/pasivosApi.js', [
    ['import.meta.env.VITE_API_PASIVOS', JSON.stringify('http://localhost:8081')],
  ]);
  const llamadas = [];
  const respuesta = { idCliente: 1, cedulaCliente: '1712345678' };
  const fetchAnterior = globalThis.fetch;
  try {
    globalThis.fetch = async (...argumentos) => {
      llamadas.push(argumentos);
      return { ok: true, json: async () => respuesta };
    };
    assert.deepEqual(await api.iniciarSesion('1712345678'), respuesta);
    assert.equal(llamadas[0][0], 'http://localhost:8081/api/autenticacion/login');
    assert.deepEqual(JSON.parse(llamadas[0][1].body), { cedulaCliente: '1712345678' });
    assert.equal(llamadas[0][1].method, 'POST');
    await api.obtenerCuentas('1712345678');
    assert.equal(llamadas[1][0], 'http://localhost:8081/api/clientes/1712345678/cuentas');
    await api.obtenerMovimientos('001/002', 2, 5);
    assert.equal(llamadas[2][0], 'http://localhost:8081/api/cuentas/001%2F002/movimientos?page=2&size=5');
    await api.obtenerMovimientos('001');
    assert.ok(llamadas[3][0].endsWith('?page=0&size=10'));
    globalThis.fetch = async () => ({ ok: false, json: async () => ({ detail: 'Cuenta no encontrada.' }) });
    await assert.rejects(api.obtenerCuentas('0'), { message: 'Cuenta no encontrada.' });
    globalThis.fetch = async () => { throw new Error('Red'); };
    await assert.rejects(api.iniciarSesion('0'), { message: 'No se pudo conectar con el servidor.' });
  } finally {
    globalThis.fetch = fetchAnterior;
  }
});

test('sesiones existentes conservan su clave y formato', async () => {
  const almacenamiento = new Map([['bq-session', '{"idCliente":1,"nombresCliente":"Ana"}']]);
  const anterior = globalThis.sessionStorage;
  globalThis.sessionStorage = {
    getItem: clave => almacenamiento.get(clave) ?? null,
    setItem: (clave, valor) => almacenamiento.set(clave, valor),
    removeItem: clave => almacenamiento.delete(clave),
  };
  try {
    const sesion = await cargarModulo('../src/servicios/sesion.js');
    assert.deepEqual(sesion.obtenerSesion(), { idCliente: 1, nombresCliente: 'Ana' });
    sesion.guardarSesion({ idCliente: 2 });
    assert.equal(almacenamiento.get('bq-session'), '{"idCliente":2}');
    sesion.borrarSesion();
    assert.equal(sesion.obtenerSesion(), null);
  } finally {
    globalThis.sessionStorage = anterior;
  }
});
