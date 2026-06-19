// Cliente HTTP central del front-end.
//
// Todas las peticiones salen como POST hacia el API Gateway con la cabecera
// X-HTTP-Method-Override indicando el verbo REST real. El Gateway transcribe
// ese POST al método correspondiente (GET/POST/PUT/PATCH/DELETE) antes de
// reenviarlo al microservicio. Para los endpoints protegidos se adjunta el
// token opaco en la cabecera Authorization.

import { API_URL } from '../config/environment';

const TOKEN_KEY = 'rdp-token';

export const tokenStore = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token) => localStorage.setItem(TOKEN_KEY, token),
  clear: () => localStorage.removeItem(TOKEN_KEY),
};

export async function apiRequest(method, path, { body, auth = false } = {}) {
  const headers = {
    'Content-Type': 'application/json',
    'X-HTTP-Method-Override': method,
  };

  if (auth) {
    const token = tokenStore.get();
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
  }

  const response = await fetch(`${API_URL}${path}`, {
    method: 'POST',
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (response.status === 204) {
    return null;
  }

  const text = await response.text();
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = { message: text };
    }
  }

  if (!response.ok) {
    const message = data?.message || `Error ${response.status}`;
    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}
