import { apiRequest } from './apiClient';

// Registra una compra. items: [{ bookId, quantity }]. Endpoint protegido.
export async function createOrder(userId, items) {
  return apiRequest('POST', '/api/orders', {
    auth: true,
    body: { userId, items },
  });
}

// Adapta una OrderResponse del back-end al modelo que usa OrderHistory.
function mapOrder(order) {
  return {
    id: `ORD-${order.id}`,
    date: order.createdAt ? order.createdAt.slice(0, 10) : '',
    items: (order.items ?? []).map((item) => ({
      title: `Libro #${item.bookId}`,
      quantity: item.quantity,
      price: Number(item.unitPrice),
    })),
    total: Number(order.totalAmount),
  };
}

// Recupera los pedidos recientes de un usuario (vista de perfil). Protegido.
export async function fetchRecentOrders(userId) {
  const orders = await apiRequest('GET', `/api/orders/users/${userId}/recent`, {
    auth: true,
  });
  return (orders ?? []).map(mapOrder);
}
