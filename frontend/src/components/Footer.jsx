function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="footer">
      <p>Relatos de Papel · Librería digital · {year}</p>
    </footer>
  );
}

export default Footer;
