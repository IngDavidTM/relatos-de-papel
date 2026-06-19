import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setSubmitting(true);

    const result = await login(credentials.email.trim(), credentials.password);
    setSubmitting(false);

    if (!result.success) {
      setError(result.message);
      return;
    }

    navigate('/profile');
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>Iniciar sesión</h2>
      <p className="login-hint">
        Demo — correo: claudia.rivera@relatosdepapel.com · clave: relatos123
      </p>

      <label htmlFor="email">Correo electrónico</label>
      <input
        id="email"
        name="email"
        type="email"
        value={credentials.email}
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

      <button className="btn btn-primary" type="submit" disabled={submitting}>
        {submitting ? 'Entrando…' : 'Entrar'}
      </button>

      <p className="auth-switch">
        ¿No tienes cuenta?{' '}
        <Link to="/register">Regístrate aquí</Link>
      </p>
    </form>
  );
}

export default LoginForm;
