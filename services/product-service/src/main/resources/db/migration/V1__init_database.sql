-- Sequences: create with increment 1 to match JPA allocationSize=1
CREATE SEQUENCE IF NOT EXISTS category_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS product_id_seq START WITH 1 INCREMENT BY 1;

-- Category table (id uses sequence default)
CREATE TABLE IF NOT EXISTS category (
    id integer NOT NULL PRIMARY KEY DEFAULT nextval('category_id_seq'::regclass),
    name varchar(255) NOT NULL UNIQUE,
    description text,
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp
);

-- Product table (id uses sequence default)
CREATE TABLE IF NOT EXISTS product (
    id integer NOT NULL PRIMARY KEY DEFAULT nextval('product_id_seq'::regclass),
    name varchar(255) NOT NULL,
    description text,
    available_quantity double precision NOT NULL,
    price numeric(38,2) NOT NULL,
    category_id integer REFERENCES category(id) ON DELETE SET NULL,
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp
);

