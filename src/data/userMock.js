const userMock = {
  credentials: {
    username: 'lectora',
    password: 'relatos123',
  },
  profile: {
    id: 'u-001',
    name: 'Claudia Rivera',
    email: 'claudia.rivera@relatosdepapel.com',
    avatar:
      'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=400&q=80',
    bio: 'Lectora apasionada de narrativa latinoamericana y ensayo contemporáneo.',
  },
  orders: [
    {
      id: 'ORD-5001',
      date: '2026-03-10',
      items: [
        { title: 'Rayuela', quantity: 1, price: 19.75 },
        { title: 'El Principito', quantity: 2, price: 12.6 },
      ],
      total: 44.95,
    },
    {
      id: 'ORD-5002',
      date: '2026-03-22',
      items: [{ title: '1984', quantity: 1, price: 17.8 }],
      total: 17.8,
    },
    {
      id: 'ORD-5003',
      date: '2026-04-02',
      items: [
        { title: 'La Casa de los Espíritus', quantity: 1, price: 19.4 },
        { title: 'Pedro Páramo', quantity: 1, price: 14.9 },
      ],
      total: 34.3,
    },
    {
      id: 'ORD-5004',
      date: '2026-04-15',
      items: [{ title: 'Ensayo sobre la Ceguera', quantity: 1, price: 21.4 }],
      total: 21.4,
    },
    {
      id: 'ORD-5005',
      date: '2026-04-26',
      items: [
        { title: 'Cien Años de Soledad', quantity: 1, price: 24.5 },
        { title: 'Siddhartha', quantity: 1, price: 14.5 },
      ],
      total: 39.0,
    },
  ],
};

export default userMock;
