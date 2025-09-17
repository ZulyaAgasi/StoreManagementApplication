CREATE TABLE IF NOT EXISTS products
(
    id UUID     PRIMARY KEY,
    name        TEXT NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    category    TEXT
);

CREATE TABLE IF NOT EXISTS store_products
    (
        id UUID PRIMARY KEY,
        store_id uuid NOT NULL,
        product_id uuid NOT NULL
);
