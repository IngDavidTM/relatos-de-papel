function SearchBar({ value, onChange }) {
  return (
    <div className="search-wrapper">
      <label htmlFor="search-book" className="search-label">
        Buscar por título
      </label>
      <input
        id="search-book"
        type="search"
        className="search-input"
        placeholder="Ej: Rayuela"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </div>
  );
}

export default SearchBar;
