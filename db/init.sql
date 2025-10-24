-- Roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL REFERENCES roles(id),
    dtype VARCHAR(31) NOT NULL
);

-- Products
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    price NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    status VARCHAR(20) DEFAULT 'PENDING',
    total NUMERIC(12,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table to associate Orders with Products (many-to-many)
CREATE TABLE order_products (
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id, product_id)
);

-- Payments (one-to-one with orders)
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO roles (name) VALUES 
('ADMIN'),
('CUSTOMER');

INSERT INTO users (username, email, password, role_id, dtype)
VALUES ('egonzalias', 'edwin@mail.com', '$2a$10$b0mtyGck0WAbDvyEN4n6NeFfmeqpg0RGPkBgrWwiBnxiKraqqtJwe', 1, 'ADMIN');

INSERT INTO products (name, description, category, price) VALUES
('Laptop Lenovo ThinkPad', 'Laptop empresarial con procesador Intel i5 y 8GB RAM', 'Electronics', 1200.00),
('Smartphone Samsung Galaxy S23', 'Smartphone con pantalla AMOLED y 128GB de almacenamiento', 'Electronics', 800.00),
('Libro "Java para Principiantes"', 'Guía completa para aprender Java desde cero', 'Books', 35.00),
('Cafetera Nespresso', 'Cafetera automática de cápsulas', 'Home Appliances', 150.00),
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos con cancelación de ruido', 'Electronics', 250.00),
('Sofá 3 plazas', 'Sofá moderno de tela gris', 'Furniture', 600.00),
('Mesa de comedor', 'Mesa de madera para 6 personas', 'Furniture', 450.00),
('Reloj Casio Classic', 'Reloj analógico resistente al agua', 'Accessories', 50.00),
('Botella de agua reutilizable', 'Botella de acero inoxidable 500ml', 'Accessories', 20.00),
('Tablet Apple iPad Pro', 'Tablet de 12.9 pulgadas con chip M2', 'Electronics', 1100.00),
('Mouse Logitech MX Master 3', 'Mouse ergonómico para productividad', 'Electronics', 100.00),
('Teclado mecánico Corsair', 'Teclado RGB mecánico con switches Cherry MX', 'Electronics', 150.00),
('Cámara Canon EOS R6', 'Cámara mirrorless profesional con sensor full-frame', 'Electronics', 2500.00),
('Libro "Spring Boot Avanzado"', 'Aprende a crear aplicaciones web con Spring Boot', 'Books', 40.00),
('Hervidor eléctrico Philips', 'Hervidor rápido de 1.7 litros', 'Home Appliances', 35.00),
('Silla de oficina ergonómica', 'Silla con soporte lumbar ajustable', 'Furniture', 200.00),
('Lampara de escritorio LED', 'Lámpara regulable con puerto USB', 'Home Appliances', 45.00),
('Smartwatch Apple Watch Series 9', 'Reloj inteligente con seguimiento de salud y fitness', 'Electronics', 450.00),
('Libro "Python Intermedio"', 'Avanza en Python con proyectos prácticos', 'Books', 30.00),
('Cojín decorativo', 'Cojín de 40x40 cm para sofá o cama', 'Furniture', 25.00),
('Zapatillas Nike Air Max', 'Zapatillas deportivas cómodas y resistentes', 'Clothing', 120.00),
('Chaqueta North Face', 'Chaqueta impermeable y térmica', 'Clothing', 250.00),
('Libro "Clean Code"', 'Principios de código limpio y buenas prácticas', 'Books', 45.00),
('Altavoz Bluetooth JBL', 'Altavoz portátil con batería de larga duración', 'Electronics', 90.00),
('Taza de cerámica', 'Taza para café de 350ml', 'Home Appliances', 10.00),
('Cámara web Logitech HD', 'Cámara para videollamadas y streaming', 'Electronics', 70.00),
('Lámpara de pie', 'Lámpara moderna para sala de estar', 'Furniture', 80.00),
('Silla plegable', 'Silla ligera y resistente para eventos', 'Furniture', 30.00),
('Pulsera Fitbit Charge 5', 'Pulsera inteligente para seguimiento de actividad', 'Electronics', 130.00),
('Libro "JavaScript Moderno"', 'Aprende JavaScript con proyectos prácticos', 'Books', 38.00),
('Horno de microondas LG', 'Microondas con grill y 20L de capacidad', 'Home Appliances', 120.00),
('Bolso de cuero', 'Bolso elegante para uso diario', 'Accessories', 85.00),
('Gafas de sol Ray-Ban', 'Gafas de sol clásicas estilo aviador', 'Accessories', 150.00),
('Sofá cama 2 plazas', 'Sofá cama cómodo y resistente', 'Furniture', 550.00),
('Tablet Samsung Galaxy Tab S8', 'Tablet con pantalla de 11 pulgadas y 128GB', 'Electronics', 700.00),
('Cafetera Italiana Bialetti', 'Cafetera de espresso tradicional', 'Home Appliances', 40.00),
('Libro "Aprendiendo SQL"', 'Manual práctico para dominar SQL', 'Books', 32.00),
('Mousepad XL', 'Mousepad grande para gaming', 'Electronics', 20.00),
('Camiseta Adidas', 'Camiseta deportiva de algodón', 'Clothing', 35.00),
('Libro "Data Structures"', 'Estructuras de datos y algoritmos en Java', 'Books', 50.00),
('Reloj Fossil Grant', 'Reloj analógico con correa de cuero', 'Accessories', 120.00),
('Auriculares Apple AirPods Pro', 'Auriculares inalámbricos con cancelación activa de ruido', 'Electronics', 250.00),
('Plancha Philips', 'Plancha a vapor de 2200W', 'Home Appliances', 60.00),
('Libro "Machine Learning"', 'Introducción al aprendizaje automático', 'Books', 55.00),
('Zapatillas Adidas Ultraboost', 'Zapatillas de running confortables', 'Clothing', 180.00),
('Mesa auxiliar', 'Mesa pequeña para sala o dormitorio', 'Furniture', 70.00),
('Libro "React Avanzado"', 'Aprende React con proyectos prácticos', 'Books', 42.00),
('Cámara GoPro HERO10', 'Cámara de acción resistente al agua', 'Electronics', 400.00),
('Smartphone Xiaomi Redmi Note 12', 'Smartphone con buena cámara y batería duradera', 'Electronics', 300.00),
('Set de cuchillos de cocina', 'Juego de 5 cuchillos de acero inoxidable', 'Home Appliances', 60.00),
('Libro "Kotlin desde Cero"', 'Aprende Kotlin paso a paso', 'Books', 36.00),
('Cinturón de cuero', 'Cinturón elegante para hombre', 'Accessories', 40.00),
('Silla de comedor', 'Silla moderna de madera para comedor', 'Furniture', 90.00);

