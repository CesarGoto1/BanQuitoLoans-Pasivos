const BASE_URL = import.meta.env.VITE_API_PASIVOS;

async function request(path, options) {
  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, options);
  } catch {
    throw new Error('No se pudo conectar con el servidor.');
  }

  if (!response.ok) {
    let detail = 'Ocurrió un error inesperado.';
    try {
      const body = await response.json();
      if (body.detail) {
        detail = body.detail;
      }
    } catch {
      detail = 'Ocurrió un error inesperado.';
    }
    throw new Error(detail);
  }

  return response.json();
}

export function login(identificationNumber) {
  return request('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ identificationNumber }),
  });
}

export function getAccounts(identificationNumber) {
  return request(`/api/customers/${encodeURIComponent(identificationNumber)}/accounts`);
}

export function getMovements(accountNumber, page = 0, size = 10) {
  return request(
    `/api/accounts/${encodeURIComponent(accountNumber)}/movements?page=${page}&size=${size}`
  );
}