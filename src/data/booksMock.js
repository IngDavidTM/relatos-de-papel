const booksMock = [
  {
    id: 1,
    title: 'Cien Años de Soledad',
    author: 'Gabriel García Márquez',
    code: 'RDP-001',
    price: 24.5,
    description:
      'Una saga familiar inolvidable en Macondo, donde lo real y lo fantástico conviven con naturalidad.',
    image:
      'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 2,
    title: 'La Sombra del Viento',
    author: 'Carlos Ruiz Zafón',
    code: 'RDP-002',
    price: 21.9,
    description:
      'Un misterio literario en la Barcelona de posguerra que conecta libros, secretos y memoria.',
    image:
      'https://images.unsplash.com/photo-1495640388908-05fa85288e61?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 3,
    title: 'Rayuela',
    author: 'Julio Cortázar',
    code: 'RDP-003',
    price: 19.75,
    description:
      'Una novela experimental que propone múltiples lecturas y desafía la estructura narrativa clásica.',
    image:
      'https://images.unsplash.com/photo-1524578271613-d550eacf6090?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 4,
    title: '1984',
    author: 'George Orwell',
    code: 'RDP-004',
    price: 17.8,
    description:
      'Distopía política sobre vigilancia, manipulación del lenguaje y pérdida de libertad individual.',
    image:
      'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 5,
    title: 'Orgullo y Prejuicio',
    author: 'Jane Austen',
    code: 'RDP-005',
    price: 16.4,
    description:
      'Clásico de romance e ironía social sobre primeras impresiones, clase y crecimiento personal.',
    image:
      'https://images.unsplash.com/photo-1519791883288-dc8bd696e667?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 6,
    title: 'Fahrenheit 451',
    author: 'Ray Bradbury',
    code: 'RDP-006',
    price: 18.2,
    description:
      'En un mundo donde los libros están prohibidos, un bombero comienza a cuestionar el sistema.',
    image:
      'https://images.unsplash.com/photo-1513001900722-370f803f498d?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 7,
    title: 'El Nombre de la Rosa',
    author: 'Umberto Eco',
    code: 'RDP-007',
    price: 22.3,
    description:
      'Intriga medieval entre manuscritos, filosofía y crímenes dentro de una abadía.',
    image:
      'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 8,
    title: 'Don Quijote de la Mancha',
    author: 'Miguel de Cervantes',
    code: 'RDP-008',
    price: 26.0,
    description:
      'Aventuras del ingenioso hidalgo y su fiel escudero en una sátira eterna sobre ideales y realidad.',
    image:
      'https://images.unsplash.com/photo-1496104679561-38b3b4f9f98f?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 9,
    title: 'Los Detectives Salvajes',
    author: 'Roberto Bolaño',
    code: 'RDP-009',
    price: 23.1,
    description:
      'Una travesía literaria por varias voces y ciudades en busca de una poeta desaparecida.',
    image:
      'https://images.unsplash.com/photo-1474932430478-367dbb6832c1?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 10,
    title: 'Pedro Páramo',
    author: 'Juan Rulfo',
    code: 'RDP-010',
    price: 14.9,
    description:
      'Viaje a Comala, pueblo de murmullos y fantasmas, en una obra breve y poderosa.',
    image:
      'https://images.unsplash.com/photo-1463320898484-cdee8141c787?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 11,
    title: 'Matar a un Ruiseñor',
    author: 'Harper Lee',
    code: 'RDP-011',
    price: 18.95,
    description:
      'Una mirada a la injusticia racial en el sur de EE.UU. desde la inocencia infantil.',
    image:
      'https://images.unsplash.com/photo-1455885666463-9d4a3b5a4f3b?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 12,
    title: 'Crimen y Castigo',
    author: 'Fiódor Dostoievski',
    code: 'RDP-012',
    price: 20.7,
    description:
      'Novela psicológica sobre culpa, moral y redención tras un acto extremo.',
    image:
      'https://images.unsplash.com/photo-1531988042231-d39a9cc12a9a?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 13,
    title: 'El Principito',
    author: 'Antoine de Saint-Exupéry',
    code: 'RDP-013',
    price: 12.6,
    description:
      'Fábula poética sobre la amistad, la infancia y lo esencial invisible a los ojos.',
    image:
      'https://images.unsplash.com/photo-1491841550275-ad7854e35ca6?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 14,
    title: 'La Casa de los Espíritus',
    author: 'Isabel Allende',
    code: 'RDP-014',
    price: 19.4,
    description:
      'Historia familiar latinoamericana marcada por pasiones, política y elementos sobrenaturales.',
    image:
      'https://images.unsplash.com/photo-1490633874781-1c63cc424610?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 15,
    title: 'El Alquimista',
    author: 'Paulo Coelho',
    code: 'RDP-015',
    price: 15.8,
    description:
      'Un viaje simbólico de autodescubrimiento y propósito personal.',
    image:
      'https://images.unsplash.com/photo-1515098506762-79e1384e9d8e?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 16,
    title: 'Ensayo sobre la Ceguera',
    author: 'José Saramago',
    code: 'RDP-016',
    price: 21.4,
    description:
      'Parábola intensa sobre el colapso social y la fragilidad humana ante una epidemia.',
    image:
      'https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 17,
    title: 'La Tregua',
    author: 'Mario Benedetti',
    code: 'RDP-017',
    price: 13.95,
    description:
      'Diario íntimo sobre la rutina, el amor tardío y la pérdida.',
    image:
      'https://images.unsplash.com/photo-1476275466078-4007374efbbe?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 18,
    title: 'Siddhartha',
    author: 'Hermann Hesse',
    code: 'RDP-018',
    price: 14.5,
    description:
      'Búsqueda espiritual de sentido, conocimiento y plenitud interior.',
    image:
      'https://images.unsplash.com/photo-1447069387593-a5de0862481e?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 19,
    title: 'La Metamorfosis',
    author: 'Franz Kafka',
    code: 'RDP-019',
    price: 11.9,
    description:
      'Relato sobre alienación y deshumanización a partir de una transformación imposible.',
    image:
      'https://images.unsplash.com/photo-1541963463532-d68292c34b19?auto=format&fit=crop&w=900&q=80',
  },
  {
    id: 20,
    title: 'Itinerario de un Lector',
    author: 'Ana María Matute',
    code: 'RDP-020',
    price: 16.95,
    description:
      'Recorrido ensayístico por el hábito de leer y su poder para transformar la mirada.',
    image:
      'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80',
  },
];

export default booksMock;
