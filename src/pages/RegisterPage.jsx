import { Navigate } from 'react-router-dom';
import RegisterForm from '../components/RegisterForm';
import { useAuth } from '../context/AuthContext';

function RegisterPage() {
  const { isAuthenticated } = useAuth();

  if (isAuthenticated) {
    return <Navigate to="/profile" replace />;
  }

  return (
    <section className="page-section centered">
      <RegisterForm />
    </section>
  );
}

export default RegisterPage;
