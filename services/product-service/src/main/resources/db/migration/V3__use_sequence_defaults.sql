-- V3: ensure sequences exist, set id defaults, and align sequence values to current max(id)

-- Create sequences if they don't exist (increment 1 to match JPA allocationSize)
CREATE SEQUENCE IF NOT EXISTS category_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS product_id_seq START WITH 1 INCREMENT BY 1;

-- Ensure id columns default to nextval of their sequences
ALTER TABLE category ALTER COLUMN id SET DEFAULT nextval('category_id_seq'::regclass);
ALTER TABLE product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);

-- Align sequences to current max(id) so nextval yields max(id)+1
SELECT setval('category_id_seq', COALESCE((SELECT MAX(id) FROM category), 0));
SELECT setval('product_id_seq', COALESCE((SELECT MAX(id) FROM product), 0));
