import { useAuth } from '../context/AuthContext';
import OrderHistory from '../components/OrderHistory';

function ProfilePage() {
  const { user, orders } = useAuth();

  if (!user) {
    return <p className="empty-message">No hay usuario autenticado.</p>;
  }

  return (
    <section className="page-section profile-section">
      <article className="profile-card">
        <img src={user.avatar} alt={user.name} className="profile-avatar" />
        <div>
          <h1>{user.name}</h1>
          <p>{user.email}</p>
          <p>{user.bio}</p>
        </div>
      </article>

      <OrderHistory orders={orders.slice(0, 5)} />
    </section>
  );
}

export default ProfilePage;
