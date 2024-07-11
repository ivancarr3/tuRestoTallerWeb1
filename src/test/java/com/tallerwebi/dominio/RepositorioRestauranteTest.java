package com.tallerwebi.dominio;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
@Transactional
public class RepositorioRestauranteTest {
    @Autowired
    private RepositorioRestaurante repositorioRestaurante;

    @Autowired
    private SessionFactory sessionFactory;

    private List<Restaurante> restaurantes = new ArrayList<>();

    @BeforeEach
    public void init() {
        this.restaurantes.add(crearYGuardarRestaurante("La Quintana", 4.5, "Arieta 5000", 1));
        this.restaurantes.add(crearYGuardarRestaurante("Benjamin", 3.9, "Arieta 5446", 2));
        this.restaurantes.add(crearYGuardarRestaurante("La Capilla", 4.0, "Almafuerte 1111", 3));
    }

    private Restaurante crearYGuardarRestaurante(String nombre, double estrellas, String direccion, Integer capacidad) {
        Restaurante restaurante = new Restaurante(null, nombre, estrellas, direccion, "restaurant.jpg", capacidad, -34.598940, -58.415550);
        repositorioRestaurante.guardar(restaurante);
        return restaurante;
    }

    @Test
    public void queGuardeRestauranteCorrectamente() {
        crearYGuardarRestaurante("La Quintana", 4.5, "Arieta 5000", 4);
        List<Restaurante> restaurantes = repositorioRestaurante.get();
        assertThat(restaurantes.size(), equalTo(4));
    }

    @Test
    public void queDevuelvaRestaurantePorId() {
        Restaurante restaurante = crearYGuardarRestaurante("La Quintana", 4.5, "Arieta 5000", 5);
        Long id = restaurante.getId();
        Restaurante restauranteEncontrado = repositorioRestaurante.buscar(id);
        assertNotNull(restauranteEncontrado);
        assertEquals(restaurante.getNombre(), restauranteEncontrado.getNombre());
        assertEquals(restaurante.getEstrellas(), restauranteEncontrado.getEstrellas());
        assertEquals(restaurante.getDireccion(), restauranteEncontrado.getDireccion());
    }

    @Test
    public void queAlBuscarPorEstrellasDevuelvaLosCorrespondientes() {
        Double estrellas = 4.0;
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);

        assertEquals(1, restaurantes.size());
    }

    @Test
    public void queAlBuscarPorEpacioDevuelvaLosCorrespondientes() {
        Integer espacio = 2;
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEspacio(espacio);

        assertEquals(2, restaurantes.size());
    }

    @Test
    public void queAlBuscarPorNombreDevuelvaLosCorrespondientes() {
        String nombre = "quintana";
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorNombre(nombre);

        assertEquals(1, restaurantes.size());
    }

    @Test
    public void queAlBuscarPorDireccionDevuelvaLosCorrespondientes() {
        String direccion = "Arieta";
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorDireccion(direccion);

        assertEquals(2, restaurantes.size());
    }

    @Test
    public void queOrdeneRestaurantesPorEstrellasAscendente() {
        List<Restaurante> restaurantes = repositorioRestaurante.ordenarPorEstrellas("ASC");

        assertEquals(3, restaurantes.size());
        assertEquals(3.9, restaurantes.get(0).getEstrellas());
        assertEquals(4.0, restaurantes.get(1).getEstrellas());
        assertEquals(4.5, restaurantes.get(2).getEstrellas());
    }

    @Test
    public void queOrdeneRestaurantesPorEstrellasDescendente() {
        List<Restaurante> restaurantes = repositorioRestaurante.ordenarPorEstrellas("DESC");

        assertEquals(3, restaurantes.size());
        assertEquals(4.5, restaurantes.get(0).getEstrellas());
        assertEquals(4.0, restaurantes.get(1).getEstrellas());
        assertEquals(3.9, restaurantes.get(2).getEstrellas());
    }

    @Test
    public void queActualizeRestaurante() {
        Restaurante restaurante = crearYGuardarRestaurante("La Quintana", 4.5, "Arieta 5000", 6);
        Long id = restaurante.getId();
        String nombre = "Test";
        String direccion = "calle test 3444";
        Double estrellas = 4.7;
        restaurante.setNombre(nombre);
        restaurante.setDireccion(direccion);
        restaurante.setEstrellas(estrellas);

        repositorioRestaurante.actualizar(restaurante);
        Restaurante restauranteActualizadoEncontrado = repositorioRestaurante.buscar(id);

        assertEquals(restauranteActualizadoEncontrado.getNombre(), nombre);
        assertEquals(restauranteActualizadoEncontrado.getDireccion(), direccion);
        assertEquals(restauranteActualizadoEncontrado.getEstrellas(), estrellas);
    }

    @Test
    public void queElimineRestaurante() {
        Restaurante restaurante = crearYGuardarRestaurante("La Quintana", 4.5, "Arieta 5000", 7);

        Long id = restaurante.getId();
        repositorioRestaurante.eliminar(restaurante);

        assertNull(repositorioRestaurante.buscar(id));
    }

    @Test
    public void queBusqueYOrdenePorEstrellas() {
        List<Restaurante> restaurantesAsc = repositorioRestaurante.buscarPorEstrellasYOrdenar(4.0, "ASC");
        assertEquals(2, restaurantesAsc.size());
        assertEquals(4.0, restaurantesAsc.get(0).getEstrellas());
        assertEquals(4.5, restaurantesAsc.get(1).getEstrellas());

        List<Restaurante> restaurantesDesc = repositorioRestaurante.buscarPorEstrellasYOrdenar(4.0, "DESC");
        assertEquals(2, restaurantesDesc.size());
        assertEquals(4.5, restaurantesDesc.get(0).getEstrellas());
        assertEquals(4.0, restaurantesDesc.get(1).getEstrellas());
    }

    @Test
    public void queObtengaRestaurantesDeshabilitados() {
        repositorioRestaurante.deshabilitarRestaurante(restaurantes.get(0).getId());

        List<Restaurante> restaurantesDeshabilitados = repositorioRestaurante.obtenerRestaurantesDeshabilitados();

        assertEquals(3, restaurantesDeshabilitados.size());
        assertEquals("La Quintana", restaurantesDeshabilitados.get(0).getNombre());
    }

    @Test
    public void queObtengaRestaurantesHabilitados() {
        // Deshabilitar uno de los restaurantes
        repositorioRestaurante.deshabilitarRestaurante(restaurantes.get(0).getId());

        List<Restaurante> restaurantesHabilitados = repositorioRestaurante.obtenerRestaurantesHabilitados();

        assertEquals(0, restaurantesHabilitados.size());
    }

    @Test
    public void queHabiliteRestaurante() {
        Restaurante restauranteNuevo = new Restaurante(null, "mateo", 2.0, "direccion", "restaurant.jpg", 20, -34.598940, -58.415550);
        repositorioRestaurante.guardar(restauranteNuevo);

        restauranteNuevo.setHabilitado(true);
        repositorioRestaurante.actualizar(restauranteNuevo);
        Restaurante restaurante = repositorioRestaurante.buscar(restauranteNuevo.getId());

        assertTrue(restaurante.isHabilitado());
    }

    @Test
    public void queDeshabiliteRestaurante() {
        repositorioRestaurante.deshabilitarRestaurante(restaurantes.get(0).getId());

        Restaurante restaurante = repositorioRestaurante.buscar(restaurantes.get(0).getId());

        assertFalse(restaurante.isHabilitado());
    }

    @Test
    public void queElimineRestaurantePorId() {
        Long id = restaurantes.get(0).getId();
        repositorioRestaurante.eliminarRestaurantePorId(id);

        Restaurante restaurante = repositorioRestaurante.buscar(id);

        assertNull(restaurante);
    }
}
