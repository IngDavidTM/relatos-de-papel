function withoutTrailingSlash(value) {
  return value.replace(/\/$/, '');
}

const configuredApiUrl = import.meta.env.VITE_API_URL?.trim();

if (import.meta.env.PROD && !configuredApiUrl) {
  throw new Error('Falta la variable de producción VITE_API_URL');
}

export const API_URL = withoutTrailingSlash(
  configuredApiUrl || 'http://localhost:8080'
);

const configuredWsUrl = import.meta.env.VITE_WS_URL?.trim();
export const WS_URL = withoutTrailingSlash(
  configuredWsUrl || API_URL.replace(/^http/, 'ws')
);
