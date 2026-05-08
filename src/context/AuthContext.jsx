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
  const [registeredUsers, setRegisteredUsers] = useLocalStorage('rdp-users', []);

  const login = (identifier, password) => {
    const isMockUser =
      (identifier === userMock.credentials.username ||
        identifier === userMock.profile.email) &&
      password === userMock.credentials.password;

    if (isMockUser) {
      setAuthState({ user: userMock.profile, isAuthenticated: true });
      return { success: true };
    }

    const found = registeredUsers.find(
      (u) => u.email === identifier && u.password === password
    );

    if (found) {
      const { password: _pw, ...profile } = found;
      setAuthState({ user: profile, isAuthenticated: true });
      return { success: true };
    }

    return { success: false, message: 'Credenciales inválidas. Intenta nuevamente.' };
  };

  const register = (name, email, password) => {
    const emailTaken =
      email === userMock.profile.email ||
      registeredUsers.some((u) => u.email === email);

    if (emailTaken) {
      return { success: false, message: 'Ya existe una cuenta con ese correo electrónico.' };
    }

    const newUser = {
      id: `u-${Date.now()}`,
      name,
      email,
      avatar:
        'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80',
      bio: 'Nuevo lector de Relatos de Papel.',
      password,
    };

    setRegisteredUsers((prev) => [...prev, newUser]);
    const { password: _pw, ...profile } = newUser;
    setAuthState({ user: profile, isAuthenticated: true });
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
      register,
      logout,
      orders,
      addOrder,
    }),
    [authState, orders, registeredUsers]
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
