package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioRestauranteTest {

    private static final Long EXISTING_ID = 1L;
    private static final String EXISTING_NAME = "El Club de la milanesa";
    private static final String EXISTING_ADDRESS = "Arieta 5000";
    private static final double EXISTING_RATING = 4.0;
    private static final String IMAGE = "restaurant.jpg";
    private static final int CAPACITY = 100;
    private static final double LATITUDE = -34.610000;
    private static final double LONGITUDE = -58.400000;

    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private ServicioReserva servicioReserva;
    private List<Restaurante> restaurantesMock;
    private ServicioGeocoding servicioGeocoding;

    @BeforeEach
    public void init() {
        repositorioRestaurante = mock(RepositorioRestaurante.class);
        servicioReserva = mock(ServicioReserva.class);
        servicioRestaurante = new ServicioRestauranteImpl(repositorioRestaurante, servicioReserva, servicioGeocoding);
        initRestaurantesMock();
    }

    private void initRestaurantesMock() {
        restaurantesMock = new ArrayList<>();
        restaurantesMock.add(new Restaurante(EXISTING_ID, EXISTING_NAME, EXISTING_RATING, EXISTING_ADDRESS, IMAGE, 2, LATITUDE, LONGITUDE));
        restaurantesMock.add(new Restaurante(2L, "La Farola", 4.0, "Almafuerte 3344", IMAGE, CAPACITY, LATITUDE, LONGITUDE));
        restaurantesMock.add(new Restaurante(3L, "Benjamin", 4.5, "Arieta 3344", IMAGE, CAPACITY, LATITUDE, LONGITUDE));
    }

    @Test
    public void testObtenerTodosLosRestaurantes() throws NoHayRestaurantes {
        when(repositorioRestaurante.get()).thenReturn(restaurantesMock);

        List<Restaurante> restaurantes = servicioRestaurante.get();

        assertThat(restaurantes, hasSize(3));
    }

    @Test
    public void testLanzaExcepcionSiNoExistenRestaurantes() {
        when(repositorioRestaurante.get()).thenReturn(null);

        assertThrows(NoHayRestaurantes.class, () -> servicioRestaurante.get());
    }

    @Test
    public void testObtenerRestaurantePorId() throws RestauranteNoEncontrado {
        when(repositorioRestaurante.buscar(EXISTING_ID)).thenReturn(restaurantesMock.get(0));

        Restaurante restaurante = servicioRestaurante.consultar(EXISTING_ID);

        assertEquals(EXISTING_NAME, restaurante.getNombre());
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestaurante() {
        when(repositorioRestaurante.buscar(EXISTING_ID)).thenReturn(null);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.consultar(EXISTING_ID));
    }

    @Test
    public void testBuscarRestaurantesPorNombre() throws RestauranteNoEncontrado {
        List<Restaurante> restaurantesPorNombre = List.of(restaurantesMock.get(0));
        when(repositorioRestaurante.buscarPorNombre(EXISTING_NAME)).thenReturn(restaurantesPorNombre);

        List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorNombre(EXISTING_NAME);

        assertThat(restaurantes, hasSize(1));
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestaurantesPorNombre() {
        when(repositorioRestaurante.buscarPorNombre(EXISTING_NAME)).thenReturn(restaurantesMock);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.consultarRestaurantePorNombre("La Quintana"));
    }

    @Test
    public void testBuscarRestaurantesPorDireccion() throws NoHayRestaurantes {
        List<Restaurante> restaurantesPorDireccion = List.of(restaurantesMock.get(1));
        when(repositorioRestaurante.buscarPorDireccion("Almafuerte 3344")).thenReturn(restaurantesPorDireccion);

        List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorDireccion("Almafuerte 3344");

        assertThat(restaurantes, hasSize(1));
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestaurantesPorDireccion() {
        when(repositorioRestaurante.buscarPorDireccion("Arieta 5000")).thenReturn(restaurantesMock);

        assertThrows(NoHayRestaurantes.class, () -> servicioRestaurante.consultarRestaurantePorDireccion("otra direccion"));
    }

    @Test
    public void testBuscarRestaurantesPorEstrellas() throws RestauranteNoEncontrado {
        List<Restaurante> restaurantesPorEstrella = List.of(restaurantesMock.get(1));
        when(repositorioRestaurante.buscarPorEstrellas(4.5)).thenReturn(restaurantesPorEstrella);

        List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorEstrellas(4.5);

        assertThat(restaurantes, hasSize(1));
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestaurantesPorEstrellas() {
        when(repositorioRestaurante.buscarPorEstrellas(4.0)).thenReturn(restaurantesMock);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.consultarRestaurantePorEstrellas(5.0));
    }

    @Test
    public void testBuscarRestaurantesPorEspacio() throws NoHayRestaurantes {
        List<Restaurante> restaurantesPorEspacio = List.of(restaurantesMock.get(1));
        when(repositorioRestaurante.buscarPorEspacio(4)).thenReturn(restaurantesPorEspacio);

        List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorEspacio(4);

        assertThat(restaurantes, hasSize(1));
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestaurantesPorEspacio() {
        when(repositorioRestaurante.buscarPorEspacio(1)).thenReturn(restaurantesMock);

        assertThrows(NoHayRestaurantes.class, () -> servicioRestaurante.consultarRestaurantePorEspacio(15));
    }

    @Test
    public void testOrdenarRestaurantesPorEstrellasAscendente() throws NoHayRestaurantes {
        Collections.sort(restaurantesMock, Comparator.comparingDouble(Restaurante::getEstrellas));
        when(repositorioRestaurante.ordenarPorEstrellas("ASC")).thenReturn(restaurantesMock);

        List<Restaurante> restaurantes = servicioRestaurante.consultarOrdenPorEstrellas("ASC");

        assertThat(restaurantes, hasSize(3));
        for (int i = 0; i < restaurantes.size() - 1; i++) {
            assertThat(restaurantes.get(i).getEstrellas(), lessThanOrEqualTo(restaurantes.get(i + 1).getEstrellas()));
        }
    }

    @Test
    public void testOrdenarRestaurantesPorEstrellasDescendente() throws NoHayRestaurantes {
        Collections.sort(restaurantesMock, Comparator.comparingDouble(Restaurante::getEstrellas).reversed());
        when(repositorioRestaurante.ordenarPorEstrellas("DESC")).thenReturn(restaurantesMock);

        List<Restaurante> restaurantes = servicioRestaurante.consultarOrdenPorEstrellas("DESC");

        assertThat(restaurantes, hasSize(3));
        for (int i = 0; i < restaurantes.size() - 1; i++) {
            assertThat(restaurantes.get(i).getEstrellas(), greaterThanOrEqualTo(restaurantes.get(i + 1).getEstrellas()));
        }
    }

    @Test
    public void testCrearRestaurante() throws RestauranteExistente {
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(null);
        Restaurante nuevoRestaurante = new Restaurante(EXISTING_ID, EXISTING_NAME, EXISTING_RATING, EXISTING_ADDRESS, IMAGE, CAPACITY, LATITUDE, LONGITUDE);

        servicioRestaurante.crearRestaurante(nuevoRestaurante);

        verify(repositorioRestaurante, times(1)).guardar(nuevoRestaurante);
    }

    @Test
    public void testLanzaExcepcionSiSeCreaRestauranteConMismoId() throws RestauranteExistente {
        Restaurante restauranteExistente = new Restaurante(EXISTING_ID, EXISTING_NAME, EXISTING_RATING, EXISTING_ADDRESS, IMAGE, CAPACITY, LATITUDE, LONGITUDE);
        when(repositorioRestaurante.buscar(EXISTING_ID)).thenReturn(restauranteExistente);

        Restaurante nuevoRestaurante = new Restaurante(EXISTING_ID, EXISTING_NAME, EXISTING_RATING, EXISTING_ADDRESS, IMAGE, CAPACITY, LATITUDE, LONGITUDE);

        assertThrows(RestauranteExistente.class, () -> servicioRestaurante.crearRestaurante(nuevoRestaurante));

        verify(repositorioRestaurante, never()).guardar(nuevoRestaurante);
    }

    @Test
    public void testActualizarRestaurante() throws RestauranteNoEncontrado {
        Restaurante restauranteEncontrado = restaurantesMock.get(0);
        restauranteEncontrado.setEstrellas(2.7);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(restauranteEncontrado);

        servicioRestaurante.actualizarRestaurante(restauranteEncontrado);

        verify(repositorioRestaurante, times(1)).actualizar(restauranteEncontrado);
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestauranteParaActualizar() {
        Restaurante restaurante = new Restaurante(67L, EXISTING_NAME, EXISTING_RATING, EXISTING_ADDRESS, IMAGE, CAPACITY, LATITUDE, LONGITUDE);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.actualizarRestaurante(restaurante));

        verify(repositorioRestaurante, never()).actualizar(restaurante);
    }

    @Test
    public void testEliminarRestaurante() throws RestauranteNoEncontrado {
        Restaurante restaurante = restaurantesMock.get(0);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(restaurante);

        servicioRestaurante.eliminarRestaurante(restaurante);

        verify(repositorioRestaurante, times(1)).eliminar(restaurante);
    }

    @Test
    public void testLanzaExcepcionSiNoEncuentraRestauranteParaEliminar() {
        Restaurante restaurante = restaurantesMock.get(0);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(null);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.eliminarRestaurante(restaurante));

        verify(repositorioRestaurante, never()).eliminar(restaurante);
    }
}
