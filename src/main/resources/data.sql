INSERT INTO usuario (id, email, password, rol) VALUES (null, 'test@unlam.edu.ar', 'test', 'ADMIN');
INSERT INTO restaurante (id, nombre, estrellas, direccion, imagen, capacidadMaxima, espacioDisponible) VALUES (null, 'El club de la Milanesa', 5.0, 'Arieta 5000', 'restaurant.jpg', 2, 2),
                                                                  (null, 'Mundo Milanesa', 3.0, 'Avenida Libertador 789', 'restaurant1.jpg', 100, 100),
                                                                  (null, 'La Trattoria Bella Italia', 3.0, 'Avenida Libertador 789', 'restaurant2.jpg', 100, 100),
                                                                  (null, 'La Parrilla de Don Juan', 4.0, 'Avenida Central 456', 'restaurant3.jpg', 100, 100),
                                                                  (null, 'El Sabor del Mar', 2.0, 'Calle Principal 123', 'restaurant4.jpg', 100, 100),
                                                                  (null, 'Café Parisien', 4.0, 'Paseo de las Flores 15', 'restaurant5.jpg', 100, 100);
INSERT INTO plato (id, nombre, precio, descripcion, imagen) VALUES (null, 'milanesa', 15000, 'milanesa rellena de amor', 'pizza.jpeg'),
                                                            (null, 'pizza', 15000, 'milanesa rellena de amor', 'pizza.jpeg'),
                                                            (null, 'pescado', 15000, 'milanesa rellena de amor', 'pizza.jpeg'),
                                                            (null, 'empanadas', 15000, 'milanesa rellena de amor', 'pizza.jpeg'),
                                                            (null, 'asado', 15000, 'milanesa rellena de amor', 'pizza.jpeg'),
                                                            (null, 'lechuga', 15000, 'milanesa rellena de amor', 'pizza.jpeg');
INSERT INTO reserva (id, idRestaurante, nombre, email, numeroCelular, dni, cantidadPersonas, fecha) VALUES (null, 1, "mateo", "mateo", 1234, 4321, 2, "2024-09-10");

