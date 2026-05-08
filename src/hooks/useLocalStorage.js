import { useEffect, useState } from 'react';

const isBrowser = typeof window !== 'undefined';

function useLocalStorage(key, initialValue) {
  const [value, setValue] = useState(() => {
    if (!isBrowser) {
      return initialValue;
    }

    const storedValue = window.localStorage.getItem(key);
    if (!storedValue) {
      return initialValue;
    }

    try {
      return JSON.parse(storedValue);
    } catch {
      return initialValue;
    }
  });

  useEffect(() => {
    if (!isBrowser) {
      return;
    }

    window.localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue];
}

export default useLocalStorage;
