CREATE DATABASE IF NOT EXISTS orders_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE orders_db;

CREATE TABLE IF NOT EXISTS orders (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    user_id      BIGINT        NOT NULL,
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(50)   NOT NULL DEFAULT 'COMPLETED',
    total_amount DECIMAL(10,2) NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    order_id   BIGINT        NOT NULL,
    book_id    BIGINT        NOT NULL,
    quantity   INT           NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,

    CONSTRAINT pk_order_items          PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT chk_order_items_qty     CHECK (quantity > 0),
    CONSTRAINT chk_order_items_price   CHECK (unit_price > 0)
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
