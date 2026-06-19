import { Link } from 'react-router-dom';

function LandingPage() {
  return (
    <section className="hero">
      <div className="hero-content">
        <p className="eyebrow">Proyecto transversal</p>
        <h1>Relatos de Papel</h1>
        <p>
          Descubre clásicos y contemporáneos en una librería digital pensada para lectoras y lectores
          que valoran historias memorables.
        </p>

        <div className="hero-stats" aria-label="Indicadores principales">
          <article className="hero-stat">
            <strong>20+</strong>
            <span>Títulos curados</span>
          </article>
          <article className="hero-stat">
            <strong>5★</strong>
            <span>Experiencia fluida</span>
          </article>
          <article className="hero-stat">
            <strong>100%</strong>
            <span>Diseño responsive</span>
          </article>
        </div>

        <div className="hero-actions">
          <Link to="/home" className="btn btn-primary">
            Explorar catálogo
          </Link>
          <Link to="/login" className="btn btn-secondary">
            Acceder a perfil
          </Link>
        </div>
      </div>
      <img
        className="hero-image"
        src="https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?auto=format&fit=crop&w=1200&q=80"
        alt="Estantería de libros"
      />
    </section>
  );
}

export default LandingPage;
