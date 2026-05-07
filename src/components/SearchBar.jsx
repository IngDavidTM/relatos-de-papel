function SearchBar({ value, onChange }) {
  return (
    <div className="search-wrapper">
      <label htmlFor="search-book" className="search-label">
        Buscar por título, autor o género
      </label>
      <input
        id="search-book"
        type="search"
        className="search-input"
        placeholder="Ej: Cortázar, realismo mágico, 1984…"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </div>
  );
}

export default SearchBar;
