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

    @BeforeEach
    public void init(){
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante);
    }

    @Test
    public void queSePuedanObtenerTodosLosRestaurantes(){
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direc1"));
        restaurantesMock.add(new Restaurante(2L, "nombre2", 3, "direc12"));
        restaurantesMock.add(new Restaurante(3L, "nombre3", 4, "direc13"));
        when(this.repositorioRestaurante.get()).thenReturn(restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.get();

        // verificacion
        assertThat(restaurantes.size(), equalTo(3)); // Existan 3 elementos
    }

    @Test
    public void queAlBuscarRestaurantesPorNombreDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorNombre("nombre1")).thenReturn(restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorNombre("nombre1");

        // verificacion
        assertThat(restaurantes.size(), equalTo(1)); // Existan 1 elementos
    }

    @Test
    public void queAlNoEncontrarRestaurantePorNombreLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorNombre("nombre1")).thenReturn(restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorNombre("nombre2");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorDireccionDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorDireccion("direccion1")).thenReturn(restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorDireccion("direccion1");

        // verificacion
        assertThat(restaurantes.size(), equalTo(1)); // Existan 1 elementos
    }

    @Test
    public void queAlNoEncontrarRestaurantePorDireccionLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorDireccion("nombre1")).thenReturn(restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorDireccion("direccion2");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorEstrellasDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorEstrellas(2)).thenReturn(restaurantesMock);

        // ejecucion
        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorEstrellas(2);

        // verificacion
        assertThat(restaurantes.size(), equalTo(1)); // Existan 1 elementos
    }

    @Test
    public void queAlNoEncontrarRestaurantePorEstrellasLanceExcepcion() throws RestauranteNoEncontrado {
        // preparacion
        List<Restaurante> restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioRestaurante.buscarPorEstrellas(2)).thenReturn(restaurantesMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorEstrellas(4);
        });
    }
}