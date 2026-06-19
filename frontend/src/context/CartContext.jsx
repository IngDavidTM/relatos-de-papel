import { createContext, useContext, useMemo } from 'react';
import useLocalStorage from '../hooks/useLocalStorage';

const CartContext = createContext(undefined);

export function CartProvider({ children }) {
  const [items, setItems] = useLocalStorage('rdp-cart', []);

  const addToCart = (book) => {
    setItems((prevItems) => {
      const existingItem = prevItems.find((item) => item.bookId === book.id);

      if (existingItem) {
        return prevItems.map((item) =>
          item.bookId === book.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      }

      return [
        ...prevItems,
        {
          bookId: book.id,
          title: book.title,
          price: book.price,
          image: book.image,
          quantity: 1,
        },
      ];
    });
  };

  const removeFromCart = (bookId) => {
    setItems((prevItems) => prevItems.filter((item) => item.bookId !== bookId));
  };

  const clearCart = () => {
    setItems([]);
  };

  const totalItems = useMemo(
    () => items.reduce((acc, item) => acc + item.quantity, 0),
    [items]
  );

  const totalPrice = useMemo(
    () => items.reduce((acc, item) => acc + item.price * item.quantity, 0),
    [items]
  );

  const value = useMemo(
    () => ({
      items,
      addToCart,
      removeFromCart,
      clearCart,
      totalItems,
      totalPrice,
    }),
    [items, totalItems, totalPrice]
  );

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  const context = useContext(CartContext);

  if (!context) {
    throw new Error('useCart debe usarse dentro de CartProvider');
  }

  return context;
}
