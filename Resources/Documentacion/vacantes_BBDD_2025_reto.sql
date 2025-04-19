-- Script para crear la base de datos y las tablas necesarias para el reto

DROP DATABASE IF EXISTS vacantes_BBDD_2025_RETO;
CREATE DATABASE vacantes_BBDD_2025_RETO;
USE vacantes_BBDD_2025_RETO;

DROP TABLE IF EXISTS `Solicitudes`;
DROP TABLE IF EXISTS `Vacantes`;
DROP TABLE IF EXISTS `Empresas`;
DROP TABLE IF EXISTS `Usuarios`;
DROP TABLE IF EXISTS `Categorias`;

CREATE TABLE `Categorias` (
  id_categoria INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(2000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Usuarios` (
  email VARCHAR(45) NOT NULL PRIMARY KEY,
  nombre VARCHAR(45) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  enabled INT NOT NULL DEFAULT 1,
  fecha_Registro DATE,
  rol VARCHAR(15) NOT NULL,
  CHECK(rol IN ('EMPRESA', 'ADMON', 'CLIENTE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Empresas` (
  id_empresa INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cif VARCHAR(10) NOT NULL UNIQUE,
  nombre_empresa VARCHAR(100) NOT NULL,
  direccion_fiscal VARCHAR(100),
  pais VARCHAR(45),
  email VARCHAR(45),
  FOREIGN KEY (email) REFERENCES `Usuarios` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Vacantes` (
  id_vacante INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL,
  descripcion TEXT NOT NULL,
  fecha DATE NOT NULL,
  salario DOUBLE NOT NULL,
  estatus ENUM('CREADA','CUBIERTA','CANCELADA') NOT NULL,
  destacado TINYINT NOT NULL,
  imagen VARCHAR(250) NOT NULL,
  detalles TEXT NOT NULL,
  id_Categoria INT NOT NULL,
  id_empresa INT NOT NULL,
  PRIMARY KEY (id_vacante),
  FOREIGN KEY (id_Categoria) REFERENCES `Categorias` (id_categoria),
  FOREIGN KEY (id_empresa) REFERENCES `Empresas` (id_empresa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Solicitudes` (
  id_solicitud INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  archivo VARCHAR(250) NOT NULL,
  comentarios VARCHAR(2000),
  estado TINYINT NOT NULL DEFAULT 0,
  curriculum VARCHAR(45),
  id_Vacante INT NOT NULL,
  email VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_solicitud),
  UNIQUE (id_Vacante,email),
  FOREIGN KEY (email) REFERENCES `Usuarios` (email),
  FOREIGN KEY (id_Vacante) REFERENCES `Vacantes` (id_vacante)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Script para insertar datos de prueba en la base de datos

-- Vaciado de las tablas
SET FOREIGN_KEY_CHECKS=0;

DELETE FROM Solicitudes;
DELETE FROM Vacantes;
DELETE FROM Empresas;
DELETE FROM Usuarios;
DELETE FROM Categorias;

SET FOREIGN_KEY_CHECKS=1;

-- Categorias
INSERT INTO Categorias (nombre, descripcion)
VALUES
('Tecnología', 'Sector IT'),
('Marketing', 'Promoción de productos y servicios'),
('Finanzas', 'Gestión financiera y contable'),
('Salud', 'Ámbito sanitario'),
('Construcción', 'Proyectos de edificación');

-- Usuarios
INSERT INTO Usuarios (email, nombre, apellidos, password, enabled, fecha_Registro, rol)
VALUES
('juan.garcia@example.com', 'Juan', 'García López', 'passjuan1', 1, '2025-01-10', 'EMPRESA'),
('maria.perez@example.com', 'María', 'Pérez Gutiérrez', 'passmaria', 1, '2025-01-15', 'EMPRESA'),
('luis.lopez@example.com', 'Luis', 'López Martínez', 'passluis2', 1, '2025-02-01', 'EMPRESA'),
('ana.martinez@example.com', 'Ana', 'Martínez Díaz', 'passana3', 1, '2025-02-05', 'EMPRESA'),
('carmen.rodriguez@example.com', 'Carmen', 'Rodríguez Silva', 'passcarmen', 1, '2025-03-01', 'EMPRESA'),
('eloctavo@hotmail.com', 'Pepe', 'Ruiz Santana', 'alcalacrece',1,'2025-01-27' , 'ADMON'),
('elseptimo@hotmail.com', 'Pilar', 'Ruiz', 'alcalacrece',1,'2025-01-28', 'CLIENTE'),
('elquinto@hotmail.com', 'Marcos', 'Loyola Méndez', 'alcalacrece',1, '2025-01-28','CLIENTE'),
('eltercero@hotmail.com', 'Adolfo', 'Rubio Flores', 'alcalacrece',1,'2025-01-27', 'ADMON'),
('elprimero@hotmail.com', 'Aarón', 'Rivero Gómez', 'alcalacrece',1,'2025-01-27' ,'CLIENTE');


-- Empresas
INSERT INTO Empresas (cif, nombre_empresa, direccion_fiscal, pais, email)
VALUES
('CIF11111Z', 'Innova Tech S.A.', 'Calle Principal 100', 'España', 'juan.garcia@example.com'),
('CIF22222X', 'Marketing Pro SL', 'Avenida del Mercado 45', 'España', 'maria.perez@example.com'),
('CIF33333N', 'Finanzas Globales SA', 'Paseo Bursátil 12', 'España', 'luis.lopez@example.com'),
('CIF44444M', 'Salud y Bienestar SL', 'Camino del Hospital 77', 'España', 'ana.martinez@example.com'),
('CIF55555T', 'Construcciones Modernas SL', 'Carretera de la Obra 50', 'España', 'carmen.rodriguez@example.com');

-- Vacantes
INSERT INTO Vacantes (nombre, descripcion, fecha, salario, estatus, destacado, imagen, detalles, id_Categoria, id_empresa)
VALUES
('Desarrollador Full Stack', 'Desarrollo de aplicaciones web', '2025-03-28', 30000, 'CREADA', 1, 'dev.png', 'Descripción detallada 1', 1, 1),
('Analista de Marketing', 'Estudios de mercado y estrategias', '2025-03-28', 28000, 'CREADA', 0, 'marketing.png', 'Descripción detallada 2', 2, 2),
('Contable Senior', 'Gestión contable y financiera', '2025-03-29', 32000, 'CUBIERTA', 1, 'contable.png', 'Descripción detallada 3', 3, 3),
('Enfermero/a', 'Atención sanitaria general', '2025-03-30', 27000, 'CREADA', 0, 'salud.png', 'Descripción detallada 4', 4, 4),
('Maestro de Obra', 'Supervisión en construcción', '2025-03-31', 25000, 'CANCELADA', 0, 'obra.png', 'Descripción detallada 5', 5, 5);

-- Solicitudes
INSERT INTO Solicitudes (fecha, archivo, comentarios, estado, curriculum, id_Vacante, email)
VALUES
('2025-04-01', 'cv1.pdf', 'Comentario 1', 0, 'CV1', 1, 'juan.garcia@example.com'),
('2025-04-02', 'cv2.pdf', 'Comentario 2', 1, 'CV2', 2, 'maria.perez@example.com'),
('2025-04-03', 'cv3.pdf', 'Comentario 3', 0, 'CV3', 3, 'luis.lopez@example.com'),
('2025-04-04', 'cv4.pdf', 'Comentario 4', 0, 'CV4', 4, 'ana.martinez@example.com'),
('2025-04-05', 'cv5.pdf', 'Comentario 5', 1, 'CV5', 5, 'carmen.rodriguez@example.com');