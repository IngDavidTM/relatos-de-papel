import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import CartItem from './CartItem';

function CartDrawer() {
  const [isOpen, setIsOpen] = useState(false);
  const { items, totalItems, totalPrice, removeFromCart } = useCart();

  useEffect(() => {
    if (!isOpen) {
      return;
    }

    const onEscape = (event) => {
      if (event.key === 'Escape') {
        setIsOpen(false);
      }
    };

    window.addEventListener('keydown', onEscape);
    return () => window.removeEventListener('keydown', onEscape);
  }, [isOpen]);

  return (
    <aside className="cart-drawer-container">
      <button className="btn btn-primary cart-toggle" onClick={() => setIsOpen((prev) => !prev)}>
        {isOpen ? 'Ocultar carrito' : 'Mostrar carrito'} ({totalItems})
      </button>

      {isOpen && (
        <div className="cart-drawer" role="region" aria-label="Carrito de compras">
          <h3>Tu carrito</h3>

          {items.length === 0 ? (
            <p className="empty-message">No hay libros en el carrito.</p>
          ) : (
            <>
              <ul className="cart-list">
                {items.map((item) => (
                  <CartItem key={item.bookId} item={item} onRemove={removeFromCart} />
                ))}
              </ul>
              <p className="cart-total">Total: ${totalPrice.toFixed(2)}</p>
              <Link className="btn btn-primary" to="/checkout" onClick={() => setIsOpen(false)}>
                Ir al checkout
              </Link>
            </>
          )}
        </div>
      )}
    </aside>
  );
}

export default CartDrawer;
