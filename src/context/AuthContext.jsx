import { createContext, useContext, useMemo } from 'react';
import useLocalStorage from '../hooks/useLocalStorage';
import userMock from '../data/userMock';

const AuthContext = createContext(undefined);

const initialAuthState = {
  user: null,
  isAuthenticated: false,
};

export function AuthProvider({ children }) {
  const [authState, setAuthState] = useLocalStorage('rdp-auth', initialAuthState);
  const [orders, setOrders] = useLocalStorage('rdp-orders', userMock.orders);

  const login = (username, password) => {
    const credentialsMatch =
      username === userMock.credentials.username &&
      password === userMock.credentials.password;

    if (!credentialsMatch) {
      return { success: false, message: 'Credenciales inválidas. Intenta nuevamente.' };
    }

    setAuthState({
      user: userMock.profile,
      isAuthenticated: true,
    });

    return { success: true };
  };

  const logout = () => {
    setAuthState(initialAuthState);
  };

  const addOrder = (order) => {
    setOrders((prevOrders) => [order, ...prevOrders].slice(0, 5));
  };

  const value = useMemo(
    () => ({
      user: authState.user,
      isAuthenticated: authState.isAuthenticated,
      login,
      logout,
      orders,
      addOrder,
    }),
    [authState, orders]
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
