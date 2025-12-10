CREATE DATABASE boletin_db;

USE boletin_db;

CREATE TABLE noticias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    contenido TEXT,
    pdf_path VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);