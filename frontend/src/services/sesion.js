const SESSION_KEY = 'bq-session';

export function saveSession(customer) {
  sessionStorage.setItem(SESSION_KEY, JSON.stringify(customer));
}

export function getSession() {
  const raw = sessionStorage.getItem(SESSION_KEY);
  return raw ? JSON.parse(raw) : null;
}

export function clearSession() {
  sessionStorage.removeItem(SESSION_KEY);
}