INSERT INTO usuario (nombre, apellido, fecha_nac, email, password, rol, activo, confirmationToken) VALUES ('Mateo', 'Fortuna', '2003-11-21', 'admin@admin.com', 'admin', 'ADMIN', TRUE, '');
SET @idUsuario1 = LAST_INSERT_ID();

INSERT INTO usuario (nombre, apellido, fecha_nac, email, password, rol, activo, confirmationToken) VALUES ('Mateo', 'Fortuna', '2003-11-21', 'user@user.com', 'user', 'USER', TRUE, '');
SET @idUsuario2 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('El club de la Milanesa', 5.0, 'Pres. Juan Domingo Perón 2700', 'restaurant.jpg', 2, 0, -34.671958, -58.562666);
SET @idResto1 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('Mundo Milanesa', 3.0, 'Mármol 2498', 'restaurant1.jpg', 100, 98, -34.674521, -58.564940);
SET @idResto2 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('La Trattoria Bella Italia', 3.0, 'Almafuerte 2550', 'restaurant2.jpg', 100, 98, -34.672880, -58.566442);
SET @idResto3 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('La Parrilla de Don Juan', 4.0, 'Avenida Central 456', 'restaurant3.jpg', 100, 100, -34.610000, -58.400000);
SET @idResto4 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('El Sabor del Mar', 2.0, 'Calle Principal 123', 'restaurant4.jpg', 100, 100, -34.599000, -58.430000);
SET @idResto5 = LAST_INSERT_ID();

INSERT INTO restaurante (nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible, latitud, longitud) VALUES
    ('Café Parisien', 4.0, 'Paseo de las Flores 15', 'restaurant5.jpg', 100, 100, -34.600000, -58.410000);
SET @idResto6 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('ensaladas');
SET @idCategoria1 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('hamburguesas');
SET @idCategoria2 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('milanesas');
SET @idCategoria3 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('postres');
SET @idCategoria4 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('pastas');
SET @idCategoria5 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('picadas');
SET @idCategoria6 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('sopas');
SET @idCategoria7 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('mariscos');
SET @idCategoria8 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('pizzas');
SET @idCategoria9 = LAST_INSERT_ID();

INSERT INTO categoria (descripcion) VALUES ('asado');
SET @idCategoria10 = LAST_INSERT_ID();

-- Inserción de platos para Restaurante 1
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada Cesar Clásica', 12000, 'Ensalada de lechuga romana, pollo a la parrilla y aderezo cesar', 'ensalada.jpg', @idResto1, @idCategoria1, true),
    ('Hamburguesa Especial', 15000, 'Hamburguesa con doble carne de res, queso cheddar, lechuga y tomate', 'hamburguesa.jpg', @idResto1, @idCategoria2, true),
    ('Milanesa Napolitana', 18000, 'Milanesa de res con salsa de tomate, jamón y queso derretido', 'milanesa.jpg', @idResto1, @idCategoria3, true),
    ('Tiramisú Tradicional', 8000, 'Postre italiano con capas de bizcocho, café y crema de mascarpone', 'helado.jpg', @idResto1, @idCategoria4, true),
    ('Spaghetti a la Carbonara', 16000, 'Pasta con salsa de huevo, queso parmesano y panceta', 'pasta.jpeg', @idResto1, @idCategoria5, true),
    ('Tapas Españolas', 14000, 'Variedad de tapas con jamón serrano, queso manchego, aceitunas y más', 'tapas.jpg', @idResto1, @idCategoria6, true),
    ('Sopa de Pollo Casera', 10000, 'Sopa caliente de pollo con verduras frescas', 'sopa.jpg', @idResto1, @idCategoria7, true),
    ('Paella Valenciana', 20000, 'Arroz con mariscos, pollo y especias', 'mariscos.jpg', @idResto1, @idCategoria8, true),
    ('Pizza Margarita Clásica', 13000, 'Pizza con tomate, mozzarella fresca y albahaca', 'pizza.jpeg', @idResto1, @idCategoria9, true),
    ('Costillar Asado Argentino', 22000, 'Costillar de res asado a la parrilla con chimichurri', 'asado.jpg', @idResto1, @idCategoria10, true);

-- Inserción de platos para Restaurante 2
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada Verde', 11500, 'Ensalada de mix de hojas verdes, pollo grillado y aderezo de limón', 'ensalada.jpg', @idResto2, @idCategoria1, false),
    ('Hamburguesa BBQ', 15500, 'Hamburguesa con carne de res, queso, cebolla caramelizada y salsa BBQ', 'hamburguesa.jpg', @idResto2, @idCategoria2, true),
    ('Milanesa de Pollo', 17500, 'Milanesa de pechuga de pollo con ensalada de papa y huevo', 'milanesa.jpg', @idResto2, @idCategoria3, true),
    ('Cheesecake', 9000, 'Postre de tarta de queso con mermelada de frutilla', 'helado.jpg', @idResto2, @idCategoria4, false),
    ('Fettuccine Alfredo', 15500, 'Pasta con salsa de crema, queso parmesano y pollo', 'pasta.jpeg', @idResto2, @idCategoria5, true),
    ('Tapas de Mar', 14500, 'Tapas con calamares, gambas y alioli', 'tapas.jpg', @idResto2, @idCategoria6, false),
    ('Crema de Champiñones', 10500, 'Sopa de champiñones con crema y perejil', 'sopa.jpg', @idResto2, @idCategoria7, true),
    ('Arroz con Pollo', 19000, 'Arroz cocido con pollo, pimientos y guisantes', 'mariscos.jpg', @idResto2, @idCategoria8, true),
    ('Pizza Pepperoni', 13500, 'Pizza con rodajas de pepperoni y mozzarella', 'pizza.jpeg', @idResto2, @idCategoria9, true),
    ('Churrasco a la Brasa', 23000, 'Churrasco de res a la brasa con papas fritas', 'asado.jpg', @idResto2, @idCategoria10, true);

-- Inserción de platos para Restaurante 3
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada Griega', 12500, 'Ensalada con tomate, pepino, cebolla, queso feta y aceitunas', 'ensalada.jpg', @idResto3, @idCategoria1, true),
    ('Hamburguesa Texana', 16000, 'Hamburguesa con carne de res, queso cheddar, jalapeños y salsa ranch', 'hamburguesa.jpg', @idResto3, @idCategoria2, true),
    ('Milanesa de Cerdo', 18500, 'Milanesa de cerdo con papas al horno', 'milanesa.jpg', @idResto3, @idCategoria3, true),
    ('Brownie con Helado', 9500, 'Brownie de chocolate con helado de vainilla', 'helado.jpg', @idResto3, @idCategoria4, true),
    ('Ravioles de Espinaca', 16500, 'Ravioles rellenos de espinaca y ricota con salsa de tomate', 'pasta.jpeg', @idResto3, @idCategoria5, true),
    ('Pinchos Mixtos', 15000, 'Pinchos de carne y vegetales a la parrilla', 'tapas.jpg', @idResto3, @idCategoria6, true),
    ('Sopa de Mariscos', 11000, 'Sopa con variedad de mariscos frescos', 'sopa.jpg', @idResto3, @idCategoria7, true),
    ('Cazuela de Mariscos', 21000, 'Cazuela con mezcla de mariscos y salsa de tomate', 'mariscos.jpg', @idResto3, @idCategoria8, true),
    ('Pizza Hawaiana', 14000, 'Pizza con jamón, piña y mozzarella', 'pizza.jpeg', @idResto3, @idCategoria9, true),
    ('Parrillada Mixta', 24000, 'Variedad de carnes a la parrilla con papas asadas', 'asado.jpg', @idResto3, @idCategoria10, true);

-- Inserción de platos para Restaurante 4
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada de Quinoa', 13000, 'Ensalada de quinoa con vegetales y vinagreta de limón', 'ensalada.jpg', @idResto4, @idCategoria1, true),
    ('Hamburguesa Clásica', 15000, 'Hamburguesa con carne de res, lechuga, tomate y queso', 'hamburguesa.jpg', @idResto4, @idCategoria2, true),
    ('Milanesa a Caballo', 19000, 'Milanesa de res con huevo frito y papas fritas', 'milanesa.jpg', @idResto4, @idCategoria3, true),
    ('Helado de Vainilla', 8000, 'Helado de vainilla con sirope de chocolate', 'helado.jpg', @idResto4, @idCategoria4, true),
    ('Lasagna Bolognesa', 17000, 'Lasagna con salsa bolognesa y bechamel', 'pasta.jpeg', @idResto4, @idCategoria5, true),
    ('Tabla de Quesos', 16000, 'Variedad de quesos con nueces y miel', 'tapas.jpg', @idResto4, @idCategoria6, true),
    ('Sopa de Tomate', 11500, 'Sopa caliente de tomate con albahaca', 'sopa.jpg', @idResto4, @idCategoria7, true),
    ('Pulpo a la Gallega', 22000, 'Pulpo cocido con aceite de oliva y pimentón', 'mariscos.jpg', @idResto4, @idCategoria8, true),
    ('Pizza Cuatro Quesos', 14500, 'Pizza con mezcla de cuatro quesos: mozzarella, gorgonzola, parmesano y provolone', 'pizza.jpeg', @idResto4, @idCategoria9, true),
    ('Asado de Tira', 25000, 'Tira de asado a la parrilla con ensalada', 'asado.jpg', @idResto4, @idCategoria10, true);

-- Inserción de platos para Restaurante 5
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada de Frutas', 12000, 'Ensalada de frutas frescas con yogurt', 'ensalada.jpg', @idResto5, @idCategoria1, true),
    ('Hamburguesa Vegana', 16000, 'Hamburguesa con patty de vegetales, lechuga, tomate y aguacate', 'hamburguesa.jpg', @idResto5, @idCategoria2, true),
    ('Milanesa de Berenjena', 17000, 'Milanesa de berenjena con ensalada de rúcula', 'milanesa.jpg', @idResto5, @idCategoria3, true),
    ('Panna Cotta', 8500, 'Postre italiano de nata cocida con frutos rojos', 'helado.jpg', @idResto5, @idCategoria4, true),
    ('Ñoquis de Papa', 16000, 'Ñoquis de papa con salsa de tomate y albahaca', 'pasta.jpeg', @idResto5, @idCategoria5, true),
    ('Mezze Mediterráneo', 15000, 'Variedad de aperitivos mediterráneos', 'tapas.jpg', @idResto5, @idCategoria6, true),
    ('Gazpacho Andaluz', 10500, 'Sopa fría de tomate y verduras', 'sopa.jpg', @idResto5, @idCategoria7, true),
    ('Langostinos al Ajillo', 22000, 'Langostinos salteados con ajo y perejil', 'mariscos.jpg', @idResto5, @idCategoria8, true),
    ('Pizza Vegetariana', 13500, 'Pizza con variedad de vegetales y mozzarella', 'pizza.jpeg', @idResto5, @idCategoria9, true),
    ('Morcilla a la Parrilla', 24000, 'Morcilla a la parrilla con papas asadas', 'asado.jpg', @idResto5, @idCategoria10, true);

-- Inserción de platos para Restaurante 6
INSERT INTO plato (nombre, precio, descripcion, imagen, id_restaurante, id_categoria, esRecomendado) VALUES
    ('Ensalada Caprese', 11500, 'Ensalada de tomate, mozzarella y albahaca con aceite de oliva', 'ensalada.jpg', @idResto6, @idCategoria1, true),
    ('Hamburguesa de Pollo', 15000, 'Hamburguesa con patty de pollo, lechuga, tomate y mayonesa', 'hamburguesa.jpg', @idResto6, @idCategoria2, true),
    ('Milanesa a la Suiza', 18000, 'Milanesa de res con queso gratinado y salsa de champiñones', 'milanesa.jpg', @idResto6, @idCategoria3, true),
    ('Crème Brûlée', 9500, 'Postre francés de crema con caramelo crujiente', 'helado.jpg', @idResto6, @idCategoria4, true),
    ('Canelones de Espinaca', 16500, 'Canelones rellenos de espinaca y ricota con salsa blanca', 'pasta.jpeg', @idResto6, @idCategoria5, true),
    ('Bruschettas Variadas', 14000, 'Bruschettas con diferentes toppings', 'tapas.jpg', @idResto6, @idCategoria6, true),
    ('Minestrone', 11000, 'Sopa italiana de verduras y pasta', 'sopa.jpg', @idResto6, @idCategoria7, true),
    ('Calamares a la Romana', 21000, 'Calamares fritos con alioli', 'mariscos.jpg', @idResto6, @idCategoria8, true),
    ('Pizza Napolitana', 13000, 'Pizza con tomate, ajo, orégano y mozzarella', 'pizza.jpeg', @idResto6, @idCategoria9, true),
    ('Vacío a la Parrilla', 23000, 'Vacío de res a la parrilla con chimichurri', 'asado.jpg', @idResto6, @idCategoria10, true);

/*INSERT INTO reserva (idRestaurante, nombre, email, numeroCelular, dni, cantidadPersonas, fecha, idUsuario) VALUES (@idResto1, "mateo", "ivancarr03@gmail.com", 1234, 4321, 2, "2024-09-10", @idUsuario1);
INSERT INTO reserva (idRestaurante, nombre, email, numeroCelular, dni, cantidadPersonas, fecha, idUsuario) VALUES (@idResto2, "mateo", "mateo.fortu@gmail.com", 1234, 4321, 2, "2024-06-10", @idUsuario1);
INSERT INTO reserva (idRestaurante, nombre, email, numeroCelular, dni, cantidadPersonas, fecha, idUsuario) VALUES (@idResto3, "mateo", "ivancarr03@gmail.com", 1234, 4321, 2, "2024-05-10", @idUsuario1);
INSERT INTO reserva (idRestaurante, nombre, email, numeroCelular, dni, cantidadPersonas, fecha, idUsuario) VALUES (@idResto1, "gene", "mateo.fortu@gmail.com", 1234, 4321, 2, "2024-06-11", @idUsuario2);*/