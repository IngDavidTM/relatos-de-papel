import { Link, useParams } from 'react-router-dom';
import booksMock from '../data/booksMock';
import { useCart } from '../context/CartContext';
import CartDrawer from '../components/CartDrawer';

function BookDetailPage() {
  const { id } = useParams();
  const { addToCart } = useCart();
  const book = booksMock.find((item) => item.id === Number(id));

  if (!book) {
    return (
      <section className="page-section">
        <p className="empty-message">Libro no encontrado.</p>
        <Link to="/home" className="btn btn-primary">
          Volver al catálogo
        </Link>
      </section>
    );
  }

  return (
    <section className="page-section book-detail-wrapper">
      <article className="book-detail">
        <img src={book.image} alt={book.title} className="book-detail-image" />

        <div className="book-detail-content">
          <p className="book-code">{book.code}</p>
          <h1>{book.title}</h1>
          <p className="book-author">{book.author}</p>
          <p className="book-description">{book.description}</p>
          <p className="book-price detail-price">${book.price.toFixed(2)}</p>

          <button className="btn btn-primary" onClick={() => addToCart(book)}>
            Añadir al carrito
          </button>
        </div>
      </article>

      <CartDrawer />
    </section>
  );
}

export default BookDetailPage;
