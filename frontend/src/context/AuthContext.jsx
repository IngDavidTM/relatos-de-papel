import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import useLocalStorage from '../hooks/useLocalStorage';
import { tokenStore } from '../services/apiClient';
import {
  createToken,
  fetchCurrentUser,
  registerUser,
  revokeToken,
} from '../services/authService';
import { fetchRecentOrders } from '../services/ordersService';

const AuthContext = createContext(undefined);

export function AuthProvider({ children }) {
  const [user, setUser] = useLocalStorage('rdp-user', null);
  const [orders, setOrders] = useState([]);

  const refreshOrders = useCallback(async () => {
    if (!user?.id) {
      setOrders([]);
      return;
    }
    try {
      setOrders(await fetchRecentOrders(user.id));
    } catch {
      setOrders([]);
    }
  }, [user]);

  // Carga los pedidos recientes cuando hay un usuario autenticado.
  useEffect(() => {
    refreshOrders();
  }, [refreshOrders]);

  const login = async (email, password) => {
    try {
      await createToken(email, password);
      const profile = await fetchCurrentUser();
      setUser(profile);
      return { success: true };
    } catch (error) {
      tokenStore.clear();
      const message =
        error.status === 401
          ? 'Credenciales inválidas. Intenta nuevamente.'
          : 'No se pudo iniciar sesión. Verifica tu conexión.';
      return { success: false, message };
    }
  };

  const register = async (name, email, password) => {
    try {
      await registerUser(name, email, password);
      // Auto-login tras el registro.
      await createToken(email, password);
      const profile = await fetchCurrentUser();
      setUser(profile);
      return { success: true };
    } catch (error) {
      const message =
        error.status === 409
          ? 'Ya existe una cuenta con ese correo electrónico.'
          : error.message || 'No se pudo completar el registro.';
      return { success: false, message };
    }
  };

  const logout = async () => {
    await revokeToken();
    setUser(null);
    setOrders([]);
  };

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(user),
      login,
      register,
      logout,
      orders,
      refreshOrders,
    }),
    [user, orders, refreshOrders]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth debe usarse dentro de AuthProvider');
  }

  return context;
}
