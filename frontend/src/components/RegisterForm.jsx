import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function RegisterForm() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [fields, setFields] = useState({ name: '', email: '', password: '', confirm: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFields((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');

    if (fields.password !== fields.confirm) {
      setError('Las contraseñas no coinciden.');
      return;
    }

    setSubmitting(true);
    const result = await register(fields.name.trim(), fields.email.trim(), fields.password);
    setSubmitting(false);

    if (!result.success) {
      setError(result.message);
      return;
    }

    navigate('/profile');
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>Crear cuenta</h2>

      <label htmlFor="reg-name">Nombre completo</label>
      <input
        id="reg-name"
        name="name"
        type="text"
        value={fields.name}
        onChange={handleChange}
        required
      />

      <label htmlFor="reg-email">Correo electrónico</label>
      <input
        id="reg-email"
        name="email"
        type="email"
        value={fields.email}
        onChange={handleChange}
        required
      />

      <label htmlFor="reg-password">Contraseña</label>
      <input
        id="reg-password"
        name="password"
        type="password"
        value={fields.password}
        onChange={handleChange}
        required
        minLength={6}
      />

      <label htmlFor="reg-confirm">Confirmar contraseña</label>
      <input
        id="reg-confirm"
        name="confirm"
        type="password"
        value={fields.confirm}
        onChange={handleChange}
        required
      />

      {error && <p className="error-text">{error}</p>}

      <button className="btn btn-primary" type="submit" disabled={submitting}>
        {submitting ? 'Creando cuenta…' : 'Registrarse'}
      </button>

      <p className="auth-switch">
        ¿Ya tienes cuenta?{' '}
        <Link to="/login">Inicia sesión</Link>
      </p>
    </form>
  );
}

export default RegisterForm;
