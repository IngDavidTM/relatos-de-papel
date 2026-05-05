import { Navigate } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const { isAuthenticated } = useAuth();

  if (isAuthenticated) {
    return <Navigate to="/profile" replace />;
  }

  return (
    <section className="page-section centered">
      <LoginForm />
    </section>
  );
}

export default LoginPage;
