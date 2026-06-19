import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { createOrder } from '../services/ordersService';

function CheckoutPage() {
  const navigate = useNavigate();
  const { items, totalPrice, clearCart } = useCart();
  const { refreshOrders } = useAuth();
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState('');

  const handlePayment = async () => {
    if (items.length === 0) {
      window.alert('Tu carrito está vacío. Añade libros antes de pagar.');
      return;
    }

    setError('');
    setProcessing(true);

    try {
      const orderItems = items.map((item) => ({
        bookId: item.bookId,
        quantity: item.quantity,
      }));

      await createOrder(orderItems);
      await refreshOrders();

      window.alert('Pago realizado con éxito. Recibirás un correo de confirmación.');
      clearCart();
      navigate('/profile');
    } catch (err) {
      setError(
        err.message || 'No se pudo procesar el pedido. Inténtalo de nuevo más tarde.'
      );
    } finally {
      setProcessing(false);
    }
  };

  return (
    <section className="page-section">
      <header className="section-header">
        <h1>Checkout</h1>
        <p>Revisa tu compra antes de confirmar el pago.</p>
      </header>

      {items.length === 0 ? (
        <p className="empty-message">No hay ítems en el carrito.</p>
      ) : (
        <div className="checkout-card">
          <ul className="checkout-list">
            {items.map((item) => (
              <li key={item.bookId} className="checkout-item">
                <span>{item.title}</span>
                <span>
                  {item.quantity} x ${item.price.toFixed(2)}
                </span>
              </li>
            ))}
          </ul>

          <p className="checkout-total">Total a pagar: ${totalPrice.toFixed(2)}</p>
          {error && <p className="error-text">{error}</p>}
          <button className="btn btn-primary" onClick={handlePayment} disabled={processing}>
            {processing ? 'Procesando…' : 'Pagar ahora'}
          </button>
        </div>
      )}
    </section>
  );
}

export default CheckoutPage;
