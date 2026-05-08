import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

function CheckoutPage() {
  const navigate = useNavigate();
  const { items, totalPrice, clearCart } = useCart();
  const { addOrder } = useAuth();

  const handlePayment = () => {
    if (items.length === 0) {
      window.alert('Tu carrito está vacío. Añade libros antes de pagar.');
      return;
    }

    const newOrder = {
      id: `ORD-${Date.now()}`,
      date: new Date().toISOString().split('T')[0],
      items: items.map((item) => ({
        title: item.title,
        quantity: item.quantity,
        price: item.price,
      })),
      total: Number(totalPrice.toFixed(2)),
    };

    addOrder(newOrder);
    window.alert('Pago realizado con éxito. Gracias por tu compra.');
    clearCart();
    navigate('/home');
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
          <button className="btn btn-primary" onClick={handlePayment}>
            Pagar ahora
          </button>
        </div>
      )}
    </section>
  );
}

export default CheckoutPage;
