import { apiRequest, tokenStore } from './apiClient';

// Crea un token (login). Devuelve y persiste el token opaco.
export async function createToken(email, password) {
  const data = await apiRequest('POST', '/api/auth/tokens', {
    body: { email, password },
  });
  tokenStore.set(data.accessToken);
  return data;
}

// Registra un nuevo usuario.
export async function registerUser(name, email, password) {
  return apiRequest('POST', '/api/users', {
    body: { name, email, password },
  });
}

// Obtiene el perfil del usuario autenticado (endpoint protegido).
// El Gateway resuelve el usuario a partir del token y la cabecera X-User-Id.
export async function fetchCurrentUser() {
  return apiRequest('GET', '/api/users/me', { auth: true });
}

// Renueva el token opaco actual.
export async function refreshToken() {
  const current = tokenStore.get();
  if (!current) {
    return null;
  }
  const data = await apiRequest('POST', '/api/auth/tokens/refresh', {
    body: { token: current },
  });
  tokenStore.set(data.accessToken);
  return data;
}

// Revoca el token (logout) y limpia el almacenamiento local.
export async function revokeToken() {
  const current = tokenStore.get();
  if (current) {
    try {
      await apiRequest('POST', '/api/auth/tokens/revoke', { body: { token: current } });
    } catch {
      // Aunque falle la revocación remota, limpiamos el token local.
    }
  }
  tokenStore.clear();
}
