package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioReservaTest {

    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private ServicioReserva servicioReserva;
    private RepositorioReserva repositorioReserva;
    private final Restaurante restauranteInit = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg", 7);
    private final List<Reserva> reservasMock = new ArrayList<>();
    private final Date fecha = new Date();

    @BeforeEach
    public void init(){
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.repositorioReserva = mock(RepositorioReserva.class);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante, this.servicioReserva);
        this.servicioReserva = new ServicioReservaImpl(this.repositorioReserva, this.repositorioRestaurante);
        this.reservasMock.add(new Reserva(null, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
        this.reservasMock.add(new Reserva(null, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
        this.reservasMock.add(new Reserva(null, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
    }

    @Test
    public void queSePuedanObtenerTodasLasReservas() throws NoHayReservas {
        when(this.repositorioReserva.buscarTodasLasReservas()).thenReturn(this.reservasMock);

        List<Reserva> reservas = this.servicioReserva.buscarTodasLasReservas();

        assertThat(reservas.size(), equalTo(3));
    }

    @Test
    public void queLanceExcepcionSiNoExistenReservas() throws NoHayReservas {
        when(this.repositorioReserva.buscarTodasLasReservas()).thenReturn(null);
        assertThrows(NoHayReservas.class, () -> this.servicioReserva.buscarTodasLasReservas());
    }


    @Test
    public void queSePuedaObtenerUnaReservaPorId() throws ReservaNoEncontrada {
        when(this.repositorioReserva.buscarReserva(1L)).thenReturn(this.reservasMock.get(0));

        Reserva reserva = this.servicioReserva.buscarReserva(1L);

        assertEquals(reserva.getNombre(), this.reservasMock.get(0).getNombre());
    }

    @Test
    public void queLanceExcepcionSiNoEncuentraReserva() throws ReservaNoEncontrada {
        when(this.repositorioReserva.buscarReserva(1L)).thenReturn(null);

        assertThrows(ReservaNoEncontrada.class, () -> servicioReserva.buscarReserva(1L));
    }

    @Test
    public void queSeCreeReservaCorrectamente() throws ReservaExistente, DatosInvalidosReserva, EspacioNoDisponible {
        when(repositorioReserva.buscarReserva(anyLong())).thenReturn(null);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        Reserva nuevaReserva = new Reserva(null, this.restauranteInit, "Mateo", "test@mail.com", 46997810, 45238796, 5, tomorrow);

        servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(), nuevaReserva.getEmail(), nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(), nuevaReserva.getCantidadPersonas(), nuevaReserva.getFecha());

        verify(repositorioReserva, times(1)).getReservasPorRestaurante(nuevaReserva.getRestaurante().getId());
        verify(repositorioReserva, times(1)).guardar(any());
    }
/*
    @Test
    public void queLanceExcepcionSiSeCreaUnRestauranteConElMismoId() throws ReservaExistente {
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
    }*/
}