-- Crear esquema si no existe
CREATE SCHEMA IF NOT EXISTS sm;

-- Crear tabla role
CREATE TABLE sm.role (
                         role_id INT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(255)
);

-- Insertar roles iniciales
INSERT INTO sm.role (role_id, name, description) VALUES
                                                     (1, 'ADMIN', 'Administrador del sistema'),
                                                     (2, 'USER', 'Usuario normal'),
                                                     (3, 'ADVISER', 'Usuario asesor'),
                                                     (4, 'CLIENT', 'Usuario cleinte');

-- Crear tabla users
CREATE TABLE users (
                          user_id SERIAL PRIMARY KEY,
                          name VARCHAR(100),
                          last_name VARCHAR(100),
                          birth_date DATE,
                          address VARCHAR(255),
                          email VARCHAR(100) UNIQUE NOT NULL,
                          identity_document VARCHAR(50),
                          phone_number VARCHAR(20),
                          role_id INT REFERENCES sm.role(role_id),
                          base_salary DOUBLE PRECISION,
                          password VARCHAR(255)
);

-- Insertar usuario inicial
INSERT INTO users (
    name, last_name, birth_date, address, email, identity_document, phone_number, role_id, base_salary, password
) VALUES ( 'John', 'Acevedo', '1990-01-01', 'Calle Falsa 123', 'brayanix27@gmail.com', '123456789', '3001234567', 1, 2000000.00, '$2a$10$pdMHgeff/akzZzYQxGrM7.51AjdiCStuqJBofK.qBAxGkLIIJZ0Sm'),
( 'John', 'Acevedo', '1990-01-01', 'Calle Falsa 123', 'jhedacro@hotmail.com', '123456789', '3001234567', 3, 2000000.00, '$2a$10$pdMHgeff/akzZzYQxGrM7.51AjdiCStuqJBofK.qBAxGkLIIJZ0Sm'),
         ( 'John', 'Acevedo', '1990-01-01', 'Calle Falsa 123', 'jhedacro@gmail.com', '123456789', '3001234567', 4, 2000000.00, '$2a$10$pdMHgeff/akzZzYQxGrM7.51AjdiCStuqJBofK.qBAxGkLIIJZ0Sm');

