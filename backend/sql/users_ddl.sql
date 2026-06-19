-- =============================================================
-- Relatos de Papel - Microservicio users
-- DDL de la base de datos de usuarios y autenticación
-- =============================================================
CREATE DATABASE IF NOT EXISTS users_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE users_db;

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    avatar        VARCHAR(500),
    bio           TEXT,
    role          VARCHAR(50)  NOT NULL DEFAULT 'ROLE_USER',
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT pk_users       PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);
