import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

function Navbar() {
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();
  const { totalItems } = useCart();

  const navClassName = ({ isActive }) => (isActive ? 'nav-link active' : 'nav-link');

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <header className="navbar-shell">
      <nav className="navbar">
        <Link to="/" className="brand" aria-label="Ir a la portada de Relatos de Papel">
          <span className="brand-mark">RP</span>
          <span className="brand-text">
            <strong>Relatos de Papel</strong>
            <small>Librería contemporánea</small>
          </span>
        </Link>

        <div className="nav-links">
          <NavLink to="/home" className={navClassName}>
            Catálogo
          </NavLink>
          <NavLink to="/profile" className={navClassName}>
            Perfil
          </NavLink>
          <NavLink to="/checkout" className={navClassName}>
            Checkout
          </NavLink>
        </div>

        <div className="nav-actions">
          <span className="cart-badge" aria-label="Total de ítems en carrito">
            {totalItems} libros
          </span>

          {isAuthenticated ? (
            <button className="btn btn-secondary" onClick={handleLogout}>
              Cerrar sesión
            </button>
          ) : (
            <Link to="/login" className="btn btn-secondary">
              Ingresar
            </Link>
          )}
        </div>
      </nav>
    </header>
  );
}

export default Navbar;
