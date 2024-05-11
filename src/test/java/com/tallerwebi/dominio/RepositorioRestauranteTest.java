package com.tallerwebi.dominio;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioRestauranteTest {
    @Autowired
    private RepositorioRestaurante repositorioRestaurante;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void init() {}

    @Test
    public void queGuardeRestauranteCorrectamente() {
        Restaurante restaurante = new Restaurante(1L, "La Quintana", 4.5, "Arieta 5000", "restaurant.jpg");

        repositorioRestaurante.guardar(restaurante);
        List<Restaurante> restaurantes = repositorioRestaurante.get();

        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queDevuelvaRestaurantePorId() {

        Restaurante restaurante = new Restaurante(1L, "La Quintana", 4.5, "Arieta 5000", "restaurant.jpg");

        repositorioRestaurante.guardar(restaurante);
        Long id = restaurante.getId();
        Restaurante restauranteEncontrado = repositorioRestaurante.buscar(id);

        assertNotNull(restauranteEncontrado);
        assertEquals(restaurante.getNombre(), restauranteEncontrado.getNombre());
        assertEquals(restaurante.getEstrellas(), restauranteEncontrado.getEstrellas());
        assertEquals(restaurante.getDireccion(), restauranteEncontrado.getDireccion());
    }

    @Test
    public void queActualizeRestaurante() {
        Restaurante restaurante = new Restaurante(1L, "La Quintana", 4.5, "Arieta 5000", "restaurant.jpg");

        repositorioRestaurante.guardar(restaurante);
        Long id = restaurante.getId();

        Restaurante restauranteActualizado = new Restaurante(2L, "Benjamin", 4.0, "Almafuerte 3344", "restaurant.jpg");
        restauranteActualizado.setId(id);

        repositorioRestaurante.actualizar(restauranteActualizado);
        Restaurante restauranteActualizadoEncontrado = repositorioRestaurante.buscar(id);

        assertEquals(restauranteActualizado.getNombre(), restauranteActualizadoEncontrado.getNombre());
        assertEquals(restauranteActualizado.getEstrellas(), restauranteActualizadoEncontrado.getEstrellas());
        assertEquals(restauranteActualizado.getDireccion(), restauranteActualizadoEncontrado.getDireccion());
    }

    @Test
    public void queElimineRestaurante() {

        Restaurante restaurante = new Restaurante(1L, "La Quintana", 4.5, "Arieta 5000", "restaurant.jpg");

        repositorioRestaurante.guardar(restaurante);
        Long id = restaurante.getId();
        repositorioRestaurante.eliminar(restaurante);

        assertNull(repositorioRestaurante.buscar(id));
    }
}
