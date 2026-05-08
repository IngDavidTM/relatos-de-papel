import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [credentials, setCredentials] = useState({ username: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (event) => {
    const { name, value } = event.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setError('');

    const result = login(credentials.username.trim(), credentials.password);
    if (!result.success) {
      setError(result.message);
      return;
    }

    navigate('/profile');
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>Iniciar sesión</h2>
      <p className="login-hint">Demo — usuario: lectora · clave: relatos123</p>

      <label htmlFor="username">Usuario</label>
      <input
        id="username"
        name="username"
        type="text"
        value={credentials.username}
        onChange={handleChange}
        required
      />

      <label htmlFor="password">Contraseña</label>
      <input
        id="password"
        name="password"
        type="password"
        value={credentials.password}
        onChange={handleChange}
        required
      />

      {error && <p className="error-text">{error}</p>}

      <button className="btn btn-primary" type="submit">
        Entrar
      </button>

      <p className="auth-switch">
        ¿No tienes cuenta?{' '}
        <Link to="/register">Regístrate aquí</Link>
      </p>
    </form>
  );
}

export default LoginForm;
