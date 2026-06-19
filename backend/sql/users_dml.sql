-- =============================================================
-- Relatos de Papel - Microservicio users
-- DML: usuarios de ejemplo
-- Las contraseñas se almacenan con BCrypt (nunca en claro).
--   claudia.rivera@relatosdepapel.com  ->  relatos123
--   admin@relatosdepapel.com           ->  admin123
-- =============================================================
USE users_db;

INSERT INTO users (name, email, password_hash, avatar, bio, role) VALUES
('Claudia Rivera', 'claudia.rivera@relatosdepapel.com',
 '$2b$10$3wo2tMpUc4oynkgVpnu24Om36VNPqBGGyBgYnJjDkRX7W6hEu5jgG',
 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=400&q=80',
 'Lectora apasionada de narrativa latinoamericana y ensayo contemporáneo.',
 'ROLE_USER'),
('Administrador', 'admin@relatosdepapel.com',
 '$2b$10$2WODDu3nQoAndSKFAodpoeMduoFYYeRNcG3GArFB19SWtxDXY4agm',
 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80',
 'Cuenta administradora de Relatos de Papel.',
 'ROLE_ADMIN');
