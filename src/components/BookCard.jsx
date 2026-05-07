import { useState } from 'react';
import { Link } from 'react-router-dom';

function BookCard({ book }) {
  const [hasImageError, setHasImageError] = useState(false);

  return (
    <article className="book-card">
      <div className="book-card-media">
        {hasImageError ? (
          <div className="book-card-image-fallback" aria-hidden="true">
            <span>{book.title.charAt(0)}</span>
          </div>
        ) : (
          <img
            src={book.image}
            alt={book.title}
            className="book-card-image"
            onError={() => setHasImageError(true)}
          />
        )}

        <div className="book-card-gradient" />
        <p className="book-code book-card-code">{book.code}</p>
      </div>

      <div className="book-card-content">
        <h3>{book.title}</h3>
        <p className="book-author">{book.author}</p>
        <span className="book-genre-badge">{book.genre}</span>

        <div className="book-card-footer">
          <p className="book-price">
            <span className="price-label">Precio</span>
            <strong>${book.price.toFixed(2)}</strong>
          </p>

          <Link className="btn btn-primary book-card-cta" to={`/book/${book.id}`}>
            Ver detalle
          </Link>
        </div>
      </div>
    </article>
  );
}

export default BookCard;
