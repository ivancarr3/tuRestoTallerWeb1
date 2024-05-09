package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.infraestructura.ServicioRestauranteImpl;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioRestauranteTest {

    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private List<Restaurante> restaurantesMock;

    @BeforeEach
    public void init(){
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante);
        this.restaurantesMock = new ArrayList<>();
        this.restaurantesMock.add(new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg"));
    }

    @Test
    public void queSePuedanObtenerTodosLosRestaurantes(){
        // preparacion
        this.restaurantesMock.add(new Restaurante(2L, "La Farola", 4.0, "Almafuerte 3344", "restaurant.jpg"));
        this.restaurantesMock.add(new Restaurante(3L, "Benjamin", 4.5, "Arieta 3344", "restaurant.jpg"));
        when(this.repositorioRestaurante.get()).thenReturn(this.restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.get();

        // verificacion
        assertThat(restaurantes.size(), equalTo(3));
    }

    @Test
    public void queAlBuscarRestaurantesPorNombreDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorNombre("El Club de la milanesa")).thenReturn(this.restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorNombre("El Club de la milanesa");

        // verificacion
        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantePorNombreLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorNombre("El Club de la milanesa")).thenReturn(this.restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorNombre("La Quintana");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorDireccionDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorDireccion("Almafuerte 3344")).thenReturn(this.restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorDireccion("Almafuerte 3344");

        // verificacion
        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantePorDireccionLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorDireccion("Arieta 5000")).thenReturn(this.restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorDireccion("otra direccion");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorEstrellasDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorEstrellas(4.5)).thenReturn(this.restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorEstrellas(4.5);

        // verificacion
        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantePorEstrellasLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        when(this.repositorioRestaurante.buscarPorEstrellas(4.0)).thenReturn(this.restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorEstrellas(5.0);
        });
    }
}