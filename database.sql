CREATE TABLE users(
    id                  SERIAL NOT NULL PRIMARY KEY,
    email               VARCHAR(50) NOT NULL UNIQUE,
    name                VARCHAR(100) NOT NULL,
    password            VARCHAR(100) NOT NULL,
    token               VARCHAR(100) UNIQUE,
    token_expired_at    BIGINT
);

CREATE TABLE products(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    type          VARCHAR(20) NOT NULL,
    price         DECIMAL NOT NULL,
    stock         integer NOT NULL DEFAULT 0
);

CREATE TABLE carts(
    id            SERIAL PRIMARY KEY,
    user_id       integer NOT NULL REFERENCES users (id),
    product_id    integer NOT NULL REFERENCES products (id),
    qty           integer NOT NULL
);

CREATE TABLE orders(
    id              SERIAL PRIMARY KEY,
    user_id         integer NOT NULL REFERENCES users (id),
    date            timestamp,
    total_price     DECIMAL NOT NULL DEFAULT 0
);

CREATE TABLE order_products(
    order_id        integer NOT NULL REFERENCES orders (id),
    product_id      integer NOT NULL REFERENCES products (id),
    price           DECIMAL NOT NULL DEFAULT 0,
    qty             integer NOT NULL,
    total           integer NOT NULL,
    PRIMARY KEY (order_id, product_id)
);