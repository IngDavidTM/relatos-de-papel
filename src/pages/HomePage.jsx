import { useMemo, useState } from 'react';
import booksMock from '../data/booksMock';
import BookCard from '../components/BookCard';
import SearchBar from '../components/SearchBar';
import CartDrawer from '../components/CartDrawer';

function HomePage() {
  const [searchTerm, setSearchTerm] = useState('');

  const filteredBooks = useMemo(() => {
    const normalizedQuery = searchTerm.trim().toLowerCase();
    if (!normalizedQuery) {
      return booksMock;
    }

    return booksMock.filter((book) => book.title.toLowerCase().includes(normalizedQuery));
  }, [searchTerm]);

  return (
    <section className="page-section">
      <header className="section-header">
        <h1>Catálogo de libros</h1>
        <p>Encuentra tu próxima lectura por título.</p>
      </header>

      <SearchBar value={searchTerm} onChange={setSearchTerm} />
      <CartDrawer />

      <div className="books-grid">
        {filteredBooks.map((book) => (
          <BookCard key={book.id} book={book} />
        ))}
      </div>

      {filteredBooks.length === 0 && (
        <p className="empty-message">No hay resultados para tu búsqueda.</p>
      )}
    </section>
  );
}

export default HomePage;
