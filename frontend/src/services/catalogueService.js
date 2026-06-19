import { apiRequest } from './apiClient';

// Adapta el modelo del back-end (BookResponse) al modelo que usa la UI.
function mapBook(book) {
  return {
    id: book.id,
    title: book.title,
    author: book.author,
    code: book.isbn,
    price: Number(book.price),
    description: book.description,
    image: book.coverUrl,
    category: book.category,
    rating: book.rating != null ? Number(book.rating) : null,
    stock: book.stock,
  };
}

// Lista/busca libros visibles. Si se indica title, filtra en el servidor.
export async function fetchBooks(title) {
  const params = new URLSearchParams({ visibility: 'true', size: '60' });
  if (title && title.trim()) {
    params.set('title', title.trim());
  }
  const page = await apiRequest('GET', `/api/catalogue/books?${params.toString()}`);
  const content = page?.content ?? [];
  return content.map(mapBook);
}

// Obtiene el detalle de un libro por id.
export async function fetchBookById(id) {
  const book = await apiRequest('GET', `/api/catalogue/books/${id}`);
  return mapBook(book);
}
