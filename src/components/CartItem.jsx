function CartItem({ item, onRemove }) {
  return (
    <li className="cart-item">
      <img src={item.image} alt={item.title} className="cart-item-image" />
      <div className="cart-item-content">
        <h4>{item.title}</h4>
        <p>Cantidad: {item.quantity}</p>
        <p>Subtotal: ${(item.price * item.quantity).toFixed(2)}</p>
      </div>
      <button className="btn btn-danger" onClick={() => onRemove(item.bookId)}>
        Eliminar
      </button>
    </li>
  );
}

export default CartItem;
