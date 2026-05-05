function formatOrderDate(dateString) {
  const [year, month, day] = dateString.split('-');
  if (!year || !month || !day) {
    return dateString;
  }

  return `${day}/${month}/${year}`;
}

function getTotalUnits(items) {
  return items.reduce((acc, item) => acc + item.quantity, 0);
}

function OrderHistory({ orders }) {
  if (orders.length === 0) {
    return <p className="empty-message">Aún no tienes pedidos registrados.</p>;
  }

  return (
    <section className="order-history">
      <div className="order-history-top">
        <h3>Últimos 5 pedidos</h3>
        <span className="orders-count">{orders.length} pedidos</span>
      </div>

      <div className="orders-grid">
        {orders.map((order) => (
          <article key={order.id} className="order-card">
            <header className="order-card-head">
              <div>
                <p className="order-id">{order.id}</p>
                <p className="order-date">{formatOrderDate(order.date)}</p>
              </div>
              <span className="order-units">{getTotalUnits(order.items)} libros</span>
            </header>

            <ul className="order-items">
              {order.items.map((item, index) => (
                <li key={`${order.id}-${item.title}-${index}`} className="order-item-row">
                  <span className="order-item-title">{item.title}</span>
                  <span className="order-item-meta">
                    {item.quantity} x ${item.price.toFixed(2)}
                  </span>
                </li>
              ))}
            </ul>

            <footer className="order-card-footer">
              <span>Total pagado</span>
              <strong>${order.total.toFixed(2)}</strong>
            </footer>
          </article>
        ))}
      </div>
    </section>
  );
}

export default OrderHistory;
