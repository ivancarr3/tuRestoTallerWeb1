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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioRestauranteTest {

    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private ServicioReserva servicioReserva;
    private List<Restaurante> restaurantesMock;

    @BeforeEach
    public void init(){
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante, this.servicioReserva);
        this.restaurantesMock = new ArrayList<>();
        this.restaurantesMock.add(new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 2));
        this.restaurantesMock.add(new Restaurante(2L, "La Farola", 4.0, "Almafuerte 3344", "restaurant.jpg", 100));
        this.restaurantesMock.add(new Restaurante(3L, "Benjamin", 4.5, "Arieta 3344", "restaurant.jpg", 100));
    }

    @Test
    public void queSePuedanObtenerTodosLosRestaurantes() throws NoHayRestaurantes {
        when(this.repositorioRestaurante.get()).thenReturn(this.restaurantesMock);

        List<Restaurante> restaurantes = this.servicioRestaurante.get();

        assertThat(restaurantes.size(), equalTo(3));
    }

    @Test
    public void queLanceExcepcionSiNoExistenRestaurantes() throws NoHayRestaurantes {
        when(this.repositorioRestaurante.get()).thenReturn(null);
        assertThrows(NoHayRestaurantes.class, () -> this.servicioRestaurante.get());
    }

    @Test
    public void queSePuedaObtenerUnRestaurantePorId() throws RestauranteNoEncontrado {
        when(this.repositorioRestaurante.buscar(1L)).thenReturn(this.restaurantesMock.get(0));

        Restaurante restaurante = this.servicioRestaurante.consultar(1L);

        assertEquals(restaurante.getNombre(), this.restaurantesMock.get(0).getNombre());
    }

    @Test
    public void queLanceExcepcionSiNoEncuentraRestaurante() throws RestauranteNoEncontrado {
        when(this.repositorioRestaurante.buscar(1L)).thenReturn(null);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.consultar(1L));
    }

    @Test
    public void queAlBuscarRestaurantesPorNombreDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        List<Restaurante> restaurantesPorNombre = List.of(this.restaurantesMock.get(0));
        when(this.repositorioRestaurante.buscarPorNombre("El Club de la milanesa")).thenReturn(restaurantesPorNombre);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorNombre("El Club de la milanesa");

        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantesPorNombreLanceExcepcion() throws RestauranteNoEncontrado {
        when(this.repositorioRestaurante.buscarPorNombre("El Club de la milanesa")).thenReturn(this.restaurantesMock);

        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorNombre("La Quintana");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorDireccionDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {
        List<Restaurante> restaurantesPorDireccion = List.of(this.restaurantesMock.get(1));

        when(this.repositorioRestaurante.buscarPorDireccion("Almafuerte 3344")).thenReturn(restaurantesPorDireccion);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorDireccion("Almafuerte 3344");

        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantesPorDireccionLanceExcepcion() throws RestauranteNoEncontrado {
        when(this.repositorioRestaurante.buscarPorDireccion("Arieta 5000")).thenReturn(this.restaurantesMock);

        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorDireccion("otra direccion");
        });
    }

    @Test
    public void queAlBuscarRestaurantesPorEstrellasDevuelvaLosCorrespondientes() throws RestauranteNoEncontrado {

        List<Restaurante> restaurantesPorEstrella = List.of(this.restaurantesMock.get(1));
        when(this.repositorioRestaurante.buscarPorEstrellas(4.5)).thenReturn(restaurantesPorEstrella);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorEstrellas(4.5);
        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlBuscarRestaurantesPorEspacioDevuelvaLosCorrespondientes() throws NoHayRestaurantes {

        List<Restaurante> restaurantesPorEspacio = List.of(this.restaurantesMock.get(1));
        when(this.repositorioRestaurante.buscarPorEspacio(4)).thenReturn(restaurantesPorEspacio);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarRestaurantePorEspacio(4);
        assertThat(restaurantes.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarRestaurantesPorEspacioLanceExcepcion() throws NoHayRestaurantes {
        when(this.repositorioRestaurante.buscarPorEspacio(1)).thenReturn(this.restaurantesMock);

        assertThrows(NoHayRestaurantes.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorEspacio(15);
        });
    }

    @Test
    public void queAlNoEncontrarRestaurantesPorEstrellasLanceExcepcion() throws RestauranteNoEncontrado {
        when(this.repositorioRestaurante.buscarPorEstrellas(4.0)).thenReturn(this.restaurantesMock);

        assertThrows(RestauranteNoEncontrado.class, () -> {
            this.servicioRestaurante.consultarRestaurantePorEstrellas(5.0);
        });
    }

    @Test
    public void queOrdeneRestaurantesPorEstrellasAscendente() throws NoHayRestaurantes {
        Collections.sort(this.restaurantesMock, Comparator.comparingDouble(Restaurante::getEstrellas));
        when(this.repositorioRestaurante.ordenarPorEstrellas("ASC")).thenReturn(this.restaurantesMock);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarOrdenPorEstrellas("ASC");

        assertThat(restaurantes.size(), equalTo(3));
        for (int i = 0; i < restaurantes.size() - 1; i++) {
            assertThat(restaurantes.get(i).getEstrellas(), lessThanOrEqualTo(restaurantes.get(i + 1).getEstrellas()));
        }
    }

    @Test
    public void queOrdeneRestaurantesPorEstrellasDescendente() throws NoHayRestaurantes {
        Collections.sort(this.restaurantesMock, Comparator.comparingDouble(Restaurante::getEstrellas).reversed());
        when(this.repositorioRestaurante.ordenarPorEstrellas("DESC")).thenReturn(this.restaurantesMock);

        List<Restaurante> restaurantes = this.servicioRestaurante.consultarOrdenPorEstrellas("DESC");

        assertThat(restaurantes.size(), equalTo(3));
        for (int i = 0; i < restaurantes.size() - 1; i++) {
            assertThat(restaurantes.get(i).getEstrellas(), greaterThanOrEqualTo(restaurantes.get(i + 1).getEstrellas()));
        }
    }

    @Test
    public void queSeCreeRestauranteCorrectamente() throws RestauranteExistente {
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(null);
        Restaurante nuevoRestaurante = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 100);

        servicioRestaurante.crearRestaurante(nuevoRestaurante);

        verify(repositorioRestaurante, times(1)).guardar(nuevoRestaurante);
    }

    @Test
    public void queLanceExcepcionSiSeCreaUnRestauranteConElMismoId() throws RestauranteExistente {
        Restaurante restauranteExistente = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 100);
        when(repositorioRestaurante.buscar(1L)).thenReturn(restauranteExistente);

        Restaurante nuevoRestaurante = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 100);

        assertThrows(RestauranteExistente.class, () -> servicioRestaurante.crearRestaurante(nuevoRestaurante));

        verify(repositorioRestaurante, never()).guardar(nuevoRestaurante);
    }

    @Test
    public void queSeActualizeRestauranteCorrectamente() throws RestauranteNoEncontrado {
        Restaurante restauranteEncontrado = this.restaurantesMock.get(0);
        restauranteEncontrado.setEstrellas(2.7);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(restauranteEncontrado);

        servicioRestaurante.actualizarRestaurante(restauranteEncontrado);

        verify(repositorioRestaurante, times(1)).actualizar(restauranteEncontrado);
    }

    @Test
    public void queLanceExcepcionSiQuiereActualizarUnRestauranteQueNoExiste() throws RestauranteNoEncontrado {
        Restaurante restaurante = new Restaurante(67L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 100);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.actualizarRestaurante(restaurante));
        verify(repositorioRestaurante, never()).actualizar(restaurante);
    }

    @Test
    public void queSeElimineRestauranteCorrectamente() throws RestauranteNoEncontrado {
        Restaurante restaurante = this.restaurantesMock.get(0);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(restaurante);

        servicioRestaurante.eliminarRestaurante(restaurante);

        verify(repositorioRestaurante, times(1)).eliminar(restaurante);
    }

    @Test
    public void queLanceExcepcionSiQuiereEliminarUnRestauranteQueNoExiste() throws RestauranteNoEncontrado {
        Restaurante restaurante = this.restaurantesMock.get(0);
        when(repositorioRestaurante.buscar(anyLong())).thenReturn(null);

        assertThrows(RestauranteNoEncontrado.class, () -> servicioRestaurante.eliminarRestaurante(restaurante));
        verify(repositorioRestaurante, never()).eliminar(restaurante);
    }
}