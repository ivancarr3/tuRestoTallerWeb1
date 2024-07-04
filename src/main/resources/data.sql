INSERT INTO
    usuario (email, password, rol)
VALUES
    ('test@unlam.edu.ar', 'test', 'ADMIN');

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'El club de la Milanesa',
        5.0,
        'Arieta 5000',
        'restaurant.jpg',
        2,
        2
    );

SET
    @idResto1 = LAST_INSERT_ID();

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'Mundo Milanesa',
        3.0,
        'Avenida Libertador 789',
        'restaurant1.jpg',
        100,
        100
    );

SET
    @idResto2 = LAST_INSERT_ID();

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'La Trattoria Bella Italia',
        3.0,
        'Avenida Libertador 789',
        'restaurant2.jpg',
        100,
        100
    );

SET
    @idResto3 = LAST_INSERT_ID();

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'La Parrilla de Don Juan',
        4.0,
        'Avenida Central 456',
        'restaurant3.jpg',
        100,
        100
    );

SET
    @idResto4 = LAST_INSERT_ID();

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'El Sabor del Mar',
        2.0,
        'Calle Principal 123',
        'restaurant4.jpg',
        100,
        100
    );

SET
    @idResto5 = LAST_INSERT_ID();

INSERT INTO
    restaurante (
        nombre,
        estrellas,
        direccion,
        imagen,
        capacidadMaxima,
        espacioDisponible
    )
VALUES
    (
        'Café Parisien',
        4.0,
        'Paseo de las Flores 15',
        'restaurant5.jpg',
        100,
        100
    );

SET
    @idResto6 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Ensaladas');

SET
    @idCategoria1 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Hamburguesas');

SET
    @idCategoria2 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Milanesas');

SET
    @idCategoria3 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Postres');

SET
    @idCategoria4 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Pastas');

SET
    @idCategoria5 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Tapeos');

SET
    @idCategoria6 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Sopas');

SET
    @idCategoria7 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Mariscos');

SET
    @idCategoria8 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Pizzas');

SET
    @idCategoria9 = LAST_INSERT_ID();

INSERT INTO
    categoria (descripcion)
VALUES
    ('Asado');

SET
    @idCategoria10 = LAST_INSERT_ID();

--Restaurante 1
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada Cesar Clásica',
        12000,
        'Ensalada de lechuga romana, pollo a la parrilla y aderezo cesar',
        'ensalada.jpg',
        @idResto1,
        @idCategoria1,
        true
    ),
    (
        'Hamburguesa Especial',
        15000,
        'Hamburguesa con doble carne de res, queso cheddar, lechuga y tomate',
        'hamburguesa.jpg',
        @idResto1,
        @idCategoria2,
        true
    ),
    (
        'Milanesa Napolitana',
        18000,
        'Milanesa de res con salsa de tomate, jamón y queso derretido',
        'milanesa.jpg',
        @idResto1,
        @idCategoria3,
        true
    ),
    (
        'Tiramisú Tradicional',
        8000,
        'Postre italiano con capas de bizcocho, café y crema de mascarpone',
        'helado.jpg',
        @idResto1,
        @idCategoria4,
        true
    ),
    (
        'Spaghetti a la Carbonara',
        16000,
        'Pasta con salsa de huevo, queso parmesano y panceta',
        'pasta.jpeg',
        @idResto1,
        @idCategoria5,
        true
    ),
    (
        'Tapas Españolas',
        14000,
        'Variedad de tapas con jamón serrano, queso manchego, aceitunas y más',
        'tapas.jpg',
        @idResto1,
        @idCategoria6,
        true
    ),
    (
        'Sopa de Pollo Casera',
        10000,
        'Sopa caliente de pollo con verduras frescas',
        'sopa.jpg',
        @idResto1,
        @idCategoria7,
        true
    ),
    (
        'Paella Valenciana',
        20000,
        'Arroz con mariscos, pollo y especias',
        'mariscos.jpg',
        @idResto1,
        @idCategoria8,
        true
    ),
    (
        'Pizza Margarita Clásica',
        13000,
        'Pizza con tomate, mozzarella fresca y albahaca',
        'pizza.jpeg',
        @idResto1,
        @idCategoria9,
        true
    ),
    (
        'Costillar Asado Argentino',
        22000,
        'Costillar de res asado a la parrilla con chimichurri',
        'asado.jpg',
        @idResto1,
        @idCategoria10,
        true
    );

-- Restaurante 2
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada Verde',
        11500,
        'Ensalada de mix de hojas verdes, pollo grillado y aderezo de limón',
        'ensalada.jpg',
        @idResto2,
        @idCategoria1,
        false
    ),
    (
        'Ensalada Verde',
        11500,
        'Ensalada de mix de hojas verdes, pollo grillado y aderezo de limón',
        'ensalada.jpg',
        @idResto2,
        @idCategoria1,
        false
    ),
    (
        'Ensalada Verde',
        11500,
        'Ensalada de mix de hojas verdes, pollo grillado y aderezo de limón',
        'ensalada.jpg',
        @idResto2,
        @idCategoria1,
        false
    ),
    (
        'Hamburguesa BBQ',
        15500,
        'Hamburguesa con carne de res, queso, cebolla caramelizada y salsa BBQ',
        'hamburguesa.jpg',
        @idResto2,
        @idCategoria2,
        true
    ),
    (
        'Hamburguesa BBQ',
        15500,
        'Hamburguesa con carne de res, queso, cebolla caramelizada y salsa BBQ',
        'hamburguesa.jpg',
        @idResto2,
        @idCategoria2,
        true
    ),
    (
        'Hamburguesa BBQ',
        15500,
        'Hamburguesa con carne de res, queso, cebolla caramelizada y salsa BBQ',
        'hamburguesa.jpg',
        @idResto2,
        @idCategoria2,
        true
    ),
    (
        'Milanesa de Pollo',
        17500,
        'Milanesa de pechuga de pollo con ensalada de papa y huevo',
        'milanesa.jpg',
        @idResto2,
        @idCategoria3,
        true
    ),
    (
        'Milanesa de Pollo',
        17500,
        'Milanesa de pechuga de pollo con ensalada de papa y huevo',
        'milanesa.jpg',
        @idResto2,
        @idCategoria3,
        true
    ),
    (
        'Milanesa de Pollo',
        17500,
        'Milanesa de pechuga de pollo con ensalada de papa y huevo',
        'milanesa.jpg',
        @idResto2,
        @idCategoria3,
        true
    ),
    (
        'Cheesecake',
        9000,
        'Postre de tarta de queso con mermelada de frutilla',
        'helado.jpg',
        @idResto2,
        @idCategoria4,
        false
    ),
    (
        'Cheesecake',
        9000,
        'Postre de tarta de queso con mermelada de frutilla',
        'helado.jpg',
        @idResto2,
        @idCategoria4,
        false
    ),
    (
        'Cheesecake',
        9000,
        'Postre de tarta de queso con mermelada de frutilla',
        'helado.jpg',
        @idResto2,
        @idCategoria4,
        false
    ),
    (
        'Fettuccine Alfredo',
        15500,
        'Pasta con salsa de crema, queso parmesano y pollo',
        'pasta.jpeg',
        @idResto2,
        @idCategoria5,
        true
    ),
    (
        'Fettuccine Alfredo',
        15500,
        'Pasta con salsa de crema, queso parmesano y pollo',
        'pasta.jpeg',
        @idResto2,
        @idCategoria5,
        true
    ),
    (
        'Fettuccine Alfredo',
        15500,
        'Pasta con salsa de crema, queso parmesano y pollo',
        'pasta.jpeg',
        @idResto2,
        @idCategoria5,
        true
    ),
    (
        'Tapas de Mar',
        14500,
        'Tapas con calamares, gambas y alioli',
        'tapas.jpg',
        @idResto2,
        @idCategoria6,
        false
    ),
    (
        'Tapas de Mar',
        14500,
        'Tapas con calamares, gambas y alioli',
        'tapas.jpg',
        @idResto2,
        @idCategoria6,
        false
    ),
    (
        'Tapas de Mar',
        14500,
        'Tapas con calamares, gambas y alioli',
        'tapas.jpg',
        @idResto2,
        @idCategoria6,
        false
    ),
    (
        'Crema de Champiñones',
        10500,
        'Sopa de champiñones con crema y perejil',
        'sopa.jpg',
        @idResto2,
        @idCategoria7,
        true
    ),
    (
        'Crema de Champiñones',
        10500,
        'Sopa de champiñones con crema y perejil',
        'sopa.jpg',
        @idResto2,
        @idCategoria7,
        true
    ),
    (
        'Crema de Champiñones',
        10500,
        'Sopa de champiñones con crema y perejil',
        'sopa.jpg',
        @idResto2,
        @idCategoria7,
        true
    ),
    (
        'Arroz con Pollo',
        19000,
        'Arroz cocido con pollo, pimientos y guisantes',
        'mariscos.jpg',
        @idResto2,
        @idCategoria8,
        true
    ),
    (
        'Arroz con Pollo',
        19000,
        'Arroz cocido con pollo, pimientos y guisantes',
        'mariscos.jpg',
        @idResto2,
        @idCategoria8,
        true
    ),
    (
        'Arroz con Pollo',
        19000,
        'Arroz cocido con pollo, pimientos y guisantes',
        'mariscos.jpg',
        @idResto2,
        @idCategoria8,
        true
    ),
    (
        'Pizza Pepperoni',
        13500,
        'Pizza con rodajas de pepperoni y mozzarella',
        'pizza.jpeg',
        @idResto2,
        @idCategoria9,
        true
    ),
    (
        'Pizza Pepperoni',
        13500,
        'Pizza con rodajas de pepperoni y mozzarella',
        'pizza.jpeg',
        @idResto2,
        @idCategoria9,
        true
    ),
    (
        'Pizza Pepperoni',
        13500,
        'Pizza con rodajas de pepperoni y mozzarella',
        'pizza.jpeg',
        @idResto2,
        @idCategoria9,
        true
    ),
    (
        'Churrasco a la Brasa',
        23000,
        'Churrasco de res a la brasa con papas fritas',
        'asado.jpg',
        @idResto2,
        @idCategoria10,
        true
    ),
    (
        'Churrasco a la Brasa',
        23000,
        'Churrasco de res a la brasa con papas fritas',
        'asado.jpg',
        @idResto2,
        @idCategoria10,
        true
    ),
    (
        'Churrasco a la Brasa',
        23000,
        'Churrasco de res a la brasa con papas fritas',
        'asado.jpg',
        @idResto2,
        @idCategoria10,
        true
    );

-- Restaurante 3
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada Griega',
        12500,
        'Ensalada con tomate, pepino, cebolla, queso feta y aceitunas',
        'ensalada.jpg',
        @idResto3,
        @idCategoria1,
        true
    ),
    (
        'Hamburguesa Texana',
        16000,
        'Hamburguesa con carne de res, queso cheddar, jalapeños y salsa ranch',
        'hamburguesa.jpg',
        @idResto3,
        @idCategoria2,
        true
    ),
    (
        'Milanesa de Cerdo',
        18500,
        'Milanesa de cerdo con papas al horno',
        'milanesa.jpg',
        @idResto3,
        @idCategoria3,
        true
    ),
    (
        'Brownie con Helado',
        9500,
        'Brownie de chocolate con helado de vainilla',
        'helado.jpg',
        @idResto3,
        @idCategoria4,
        true
    ),
    (
        'Ravioles de Espinaca',
        16500,
        'Ravioles rellenos de espinaca y ricota con salsa de tomate',
        'pasta.jpeg',
        @idResto3,
        @idCategoria5,
        true
    ),
    (
        'Pinchos Morunos',
        15000,
        'Brochetas de carne de cordero con especias',
        'tapas.jpg',
        @idResto3,
        @idCategoria6,
        true
    ),
    (
        'Gazpacho',
        11000,
        'Sopa fría de tomate, pepino y pimientos',
        'sopa.jpg',
        @idResto3,
        @idCategoria7,
        false
    ),
    (
        'Risotto de Mariscos',
        20500,
        'Arroz cremoso con mariscos y azafrán',
        'mariscos.jpg',
        @idResto3,
        @idCategoria8,
        true
    ),
    (
        'Pizza Hawaiana',
        14000,
        'Pizza con jamón, piña y mozzarella',
        'pizza.jpeg',
        @idResto3,
        @idCategoria9,
        true
    ),
    (
        'Bife de Chorizo',
        24000,
        'Bife de chorizo a la parrilla con ensalada mixta',
        'asado.jpg',
        @idResto3,
        @idCategoria10,
        true
    );

-- Restaurante 4
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada Caprese',
        13000,
        'Ensalada de tomate, mozzarella y albahaca',
        'ensalada.jpg',
        @idResto4,
        @idCategoria1,
        true
    ),
    (
        'Hamburguesa con Queso Azul',
        16500,
        'Hamburguesa con carne de res, queso azul, rúcula y cebolla',
        'hamburguesa.jpg',
        @idResto4,
        @idCategoria2,
        true
    ),
    (
        'Milanesa Suiza',
        19000,
        'Milanesa de ternera con queso suizo y champiñones',
        'milanesa.jpg',
        @idResto4,
        @idCategoria3,
        true
    ),
    (
        'Flan Casero',
        10000,
        'Postre de flan con caramelo y crema',
        'helado.jpg',
        @idResto4,
        @idCategoria4,
        true
    ),
    (
        'Lasaña Boloñesa',
        17000,
        'Lasaña con salsa boloñesa y bechamel',
        'pasta.jpeg',
        @idResto4,
        @idCategoria5,
        true
    ),
    (
        'Montaditos Variados',
        15500,
        'Pequeños bocados con jamón, chorizo y queso',
        'tapas.jpg',
        @idResto4,
        @idCategoria6,
        false
    ),
    (
        'Sopa Minestrone',
        11500,
        'Sopa de verduras con pasta y frijoles',
        'sopa.jpg',
        @idResto4,
        @idCategoria7,
        true
    ),
    (
        'Cazuela de Mariscos',
        21000,
        'Guiso de mariscos con tomate y especias',
        'mariscos.jpg',
        @idResto4,
        @idCategoria8,
        true
    ),
    (
        'Pizza Cuatro Quesos',
        14500,
        'Pizza con mozzarella, gorgonzola, parmesano y queso de cabra',
        'pizza.jpeg',
        @idResto4,
        @idCategoria9,
        true
    ),
    (
        'Parrillada Mixta',
        25000,
        'Variedad de carnes a la parrilla con guarniciones',
        'asado.jpg',
        @idResto4,
        @idCategoria10,
        true
    );

-- Restaurante 5
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada de Quinoa',
        13500,
        'Ensalada de quinoa, vegetales y aderezo de limón',
        'ensalada.jpg',
        @idResto5,
        @idCategoria1,
        true
    ),
    (
        'Hamburguesa Vegetariana',
        17000,
        'Hamburguesa con medallón de vegetales, queso y aguacate',
        'hamburguesa.jpg',
        @idResto5,
        @idCategoria2,
        true
    ),
    (
        'Milanesa de Pavo',
        19500,
        'Milanesa de pavo con puré de papas',
        'milanesa.jpg',
        @idResto5,
        @idCategoria3,
        true
    ),
    (
        'Mousse de Chocolate',
        10500,
        'Postre de mousse de chocolate con crema chantilly',
        'helado.jpg',
        @idResto5,
        @idCategoria4,
        true
    ),
    (
        'Penne a la Arrabiata',
        17500,
        'Pasta con salsa de tomate picante y ajo',
        'pasta.jpeg',
        @idResto5,
        @idCategoria5,
        true
    ),
    (
        'Croquetas de Jamón',
        16000,
        'Croquetas fritas de jamón y bechamel',
        'tapas.jpg',
        @idResto5,
        @idCategoria6,
        false
    ),
    (
        'Sopa de Cebolla',
        12000,
        'Sopa de cebolla gratinada con queso',
        'sopa.jpg',
        @idResto5,
        @idCategoria7,
        true
    ),
    (
        'Pulpo a la Gallega',
        21500,
        'Pulpo cocido con papas, pimentón y aceite de oliva',
        'mariscos.jpg',
        @idResto5,
        @idCategoria8,
        true
    ),
    (
        'Pizza Calzone',
        15000,
        'Pizza doblada con relleno de jamón, queso y champiñones',
        'pizza.jpeg',
        @idResto5,
        @idCategoria9,
        true
    ),
    (
        'Cordero Asado',
        26000,
        'Pierna de cordero asada con papas y romero',
        'asado.jpg',
        @idResto5,
        @idCategoria10,
        true
    );

-- Restaurante 6
INSERT INTO
    plato (
        nombre,
        precio,
        descripcion,
        imagen,
        id_restaurante,
        id_categoria,
        esRecomendado
    )
VALUES
    (
        'Ensalada de Frutas',
        14000,
        'Ensalada de frutas frescas con yogur',
        'ensalada.jpg',
        @idResto6,
        @idCategoria1,
        false
    ),
    (
        'Hamburguesa Gourmet',
        17500,
        'Hamburguesa con carne de res, queso brie, rúcula y cebolla caramelizada',
        'hamburguesa.jpg',
        @idResto6,
        @idCategoria2,
        true
    ),
    (
        'Milanesa a la Parmesana',
        20000,
        'Milanesa de res con salsa de tomate y queso parmesano',
        'milanesa.jpg',
        @idResto6,
        @idCategoria3,
        true
    ),
    (
        'Panna Cotta',
        11000,
        'Postre italiano de nata con coulis de frutos rojos',
        'helado.jpg',
        @idResto6,
        @idCategoria4,
        true
    ),
    (
        'Tagliatelle al Pesto',
        18000,
        'Pasta con salsa de albahaca, piñones y parmesano',
        'pasta.jpeg',
        @idResto6,
        @idCategoria5,
        true
    ),
    (
        'Patatas Bravas',
        16500,
        'Patatas fritas con salsa picante',
        'tapas.jpg',
        @idResto6,
        @idCategoria6,
        true
    ),
    (
        'Sopa de Tomate',
        12500,
        'Sopa de tomate con albahaca fresca',
        'sopa.jpg',
        @idResto6,
        @idCategoria7,
        false
    ),
    (
        'Langosta Termidor',
        22000,
        'Langosta gratinada con salsa de queso',
        'mariscos.jpg',
        @idResto6,
        @idCategoria8,
        true
    ),
    (
        'Pizza de Salami',
        15500,
        'Pizza con salami picante y mozzarella',
        'pizza.jpeg',
        @idResto6,
        @idCategoria9,
        true
    ),
    (
        'Solomillo de Cerdo',
        27000,
        'Solomillo de cerdo a la parrilla con puré de manzana',
        'asado.jpg',
        @idResto6,
        @idCategoria10,
        true
    );

INSERT INTO
    reserva (
        idRestaurante,
        nombre,
        email,
        numeroCelular,
        dni,
        cantidadPersonas,
        fecha
    )
VALUES
    (
        @idResto1,
        "mateo",
        "mateo",
        1234,
        4321,
        2,
        "2024-09-10"
    );

INSERT INTO
    reserva (
        idRestaurante,
        nombre,
        email,
        numeroCelular,
        dni,
        cantidadPersonas,
        fecha
    )
VALUES
    (
        @idResto1,
        "gene",
        "gene",
        1234,
        4321,
        2,
        "2024-06-11"
    );

INSERT INTO
    administradorderestaurante (email, password, restaurante_id)
VALUES
    ('mundomila@mila.com', '123', 2);