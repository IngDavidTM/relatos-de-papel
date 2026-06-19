import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchBookById } from '../services/catalogueService';
import { useCart } from '../context/CartContext';
import CartDrawer from '../components/CartDrawer';

function BookDetailPage() {
  const { id } = useParams();
  const { addToCart } = useCart();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    setLoading(true);
    fetchBookById(id)
      .then((data) => {
        if (active) setBook(data);
      })
      .catch(() => {
        if (active) setBook(null);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, [id]);

  if (loading) {
    return (
      <section className="page-section">
        <p className="empty-message">Cargando libro…</p>
      </section>
    );
  }

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
