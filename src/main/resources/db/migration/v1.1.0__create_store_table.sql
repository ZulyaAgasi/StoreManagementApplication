CREATE TABLE IF NOT EXISTS stores(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    location TEXT NOT NULL,
    updated_at TIMESTAMP
);
