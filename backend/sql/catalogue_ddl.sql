CREATE DATABASE IF NOT EXISTS catalogue_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE catalogue_db;

CREATE TABLE IF NOT EXISTS books (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    title            VARCHAR(255)    NOT NULL,
    author           VARCHAR(255)    NOT NULL,
    publication_date DATE,
    category         VARCHAR(100),
    isbn             VARCHAR(20),
    rating           DECIMAL(2,1),
    visibility       TINYINT(1)      NOT NULL DEFAULT 1,
    stock            INT             NOT NULL DEFAULT 0,
    price            DECIMAL(10,2)   NOT NULL,
    description      TEXT,
    cover_url        VARCHAR(500),

    CONSTRAINT pk_books            PRIMARY KEY (id),
    CONSTRAINT uq_books_isbn       UNIQUE (isbn),
    CONSTRAINT chk_books_rating    CHECK (rating IS NULL OR (rating >= 1.0 AND rating <= 5.0)),
    CONSTRAINT chk_books_stock     CHECK (stock >= 0),
    CONSTRAINT chk_books_price     CHECK (price > 0)
);
