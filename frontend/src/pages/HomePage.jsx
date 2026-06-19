import { useEffect, useState } from 'react';
import { fetchBooks } from '../services/catalogueService';
import BookCard from '../components/BookCard';
import SearchBar from '../components/SearchBar';
import CartDrawer from '../components/CartDrawer';

function HomePage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Búsqueda por título contra el back-end (con un pequeño debounce).
  useEffect(() => {
    let active = true;
    setLoading(true);
    setError('');

    const timer = setTimeout(() => {
      fetchBooks(searchTerm)
        .then((data) => {
          if (active) setBooks(data);
        })
        .catch(() => {
          if (active) setError('No se pudo cargar el catálogo. ¿Está el back-end en marcha?');
        })
        .finally(() => {
          if (active) setLoading(false);
        });
    }, 300);

    return () => {
      active = false;
      clearTimeout(timer);
    };
  }, [searchTerm]);

  return (
    <section className="page-section">
      <header className="section-header">
        <h1>Catálogo de libros</h1>
        <p>Encuentra tu próxima lectura por título.</p>
      </header>

      <SearchBar value={searchTerm} onChange={setSearchTerm} />
      <CartDrawer />

      {loading && <p className="empty-message">Cargando catálogo…</p>}
      {error && <p className="error-text">{error}</p>}

      <div className="books-grid">
        {books.map((book) => (
          <BookCard key={book.id} book={book} />
        ))}
      </div>

      {!loading && !error && books.length === 0 && (
        <p className="empty-message">No hay resultados para tu búsqueda.</p>
      )}
    </section>
  );
}

export default HomePage;
