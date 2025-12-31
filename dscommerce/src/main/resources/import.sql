-- ROLES
INSERT INTO tb_role (authority) VALUES ('ROLE_CLIENT');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

-- USERS
INSERT INTO tb_user (name, email, phone, password) VALUES ('Alex Brown', 'alex@gmail.com', '999999999', '$2a$10$WDZzEMSoI3rmKpHHnu9NHuPSht3.tciMue8c.o4R1ZivKToM96lEa');
INSERT INTO tb_user (name, email, phone, password) VALUES ('Maria Green', 'maria@gmail.com', '888888888', '$2a$10$WDZzEMSoI3rmKpHHnu9NHuPSht3.tciMue8c.o4R1ZivKToM96lEa');



-- USER ROLES
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);

-- CATEGORIES
INSERT INTO tb_category (name) VALUES ('Books');
INSERT INTO tb_category (name) VALUES ('Electronics');
INSERT INTO tb_category (name) VALUES ('Computers');

-- PRODUCTS
INSERT INTO tb_product (name, description, price, img_url, date) VALUES ('The Lord of the Rings', 'Book', 90.5, 'https://img.com/1.png', CURRENT_TIMESTAMP);
INSERT INTO tb_product (name, description, price, img_url, date) VALUES ('Smart TV', 'Television', 2190.0, 'https://img.com/2.png', CURRENT_TIMESTAMP);
INSERT INTO tb_product (name, description, price, img_url, date) VALUES ('Macbook Pro', 'Laptop', 12500.0, 'https://img.com/3.png', CURRENT_TIMESTAMP);

-- PRODUCT-CATEGORY
INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 3);

-- ORDERS
INSERT INTO tb_order (moment, status, client_id) VALUES (CURRENT_TIMESTAMP, 0, 1);
INSERT INTO tb_order (moment, status, client_id) VALUES (CURRENT_TIMESTAMP, 1, 2);

-- ORDER ITEMS
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 1, 2, 90.5);
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 2, 1, 2190.0);
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (2, 3, 1, 12500.0);

-- PAYMENTS
INSERT INTO tb_payment (moment, order_id) VALUES (CURRENT_TIMESTAMP, 2);
