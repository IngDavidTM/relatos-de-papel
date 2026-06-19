CREATE DATABASE IF NOT EXISTS catalogue_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS orders_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS users_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 'localhost' para ejecución nativa; '%' para acceso desde contenedores Docker.
CREATE USER IF NOT EXISTS 'relatos_user'@'localhost'
    IDENTIFIED BY 'relatos_password';
CREATE USER IF NOT EXISTS 'relatos_user'@'%'
    IDENTIFIED BY 'relatos_password';

GRANT ALL PRIVILEGES ON catalogue_db.* TO 'relatos_user'@'localhost';
GRANT ALL PRIVILEGES ON orders_db.*    TO 'relatos_user'@'localhost';
GRANT ALL PRIVILEGES ON users_db.*     TO 'relatos_user'@'localhost';
GRANT ALL PRIVILEGES ON catalogue_db.* TO 'relatos_user'@'%';
GRANT ALL PRIVILEGES ON orders_db.*    TO 'relatos_user'@'%';
GRANT ALL PRIVILEGES ON users_db.*     TO 'relatos_user'@'%';

FLUSH PRIVILEGES;
