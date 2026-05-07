function CartItem({ item, onRemove, onUpdate }) {
  return (
    <li className="cart-item">
      <img src={item.image} alt={item.title} className="cart-item-image" />
      <div className="cart-item-content">
        <h4>{item.title}</h4>
        <div className="cart-item-qty">
          <button
            className="qty-btn"
            onClick={() => onUpdate(item.bookId, -1)}
            aria-label="Reducir cantidad"
          >
            −
          </button>
          <span className="qty-value">{item.quantity}</span>
          <button
            className="qty-btn"
            onClick={() => onUpdate(item.bookId, +1)}
            aria-label="Aumentar cantidad"
          >
            +
          </button>
        </div>
        <p className="cart-item-subtotal">Subtotal: ${(item.price * item.quantity).toFixed(2)}</p>
      </div>
      <button className="btn btn-danger" onClick={() => onRemove(item.bookId)}>
        Eliminar
      </button>
    </li>
  );
}

export default CartItem;
