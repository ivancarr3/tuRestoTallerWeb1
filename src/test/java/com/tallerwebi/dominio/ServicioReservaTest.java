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
    private Email emailSender = new Email();
    private final Restaurante restauranteInit = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000",
            "restaurant.jpg", 7);
    private final List<Reserva> reservasMock = new ArrayList<>();
    private final Date fecha = new Date();
    private Date tomorrow = new Date();

    @BeforeEach
    public void init() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        this.tomorrow = cal.getTime();
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.repositorioReserva = mock(RepositorioReserva.class);
        this.emailSender = mock(Email.class);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante, this.servicioReserva);
        this.servicioReserva = new ServicioReservaImpl(this.repositorioReserva, this.repositorioRestaurante,
                this.emailSender);
        this.reservasMock
                .add(new Reserva(1L, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
        this.reservasMock
                .add(new Reserva(2L, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
        this.reservasMock
                .add(new Reserva(3L, this.restauranteInit, "Pepe", "test@mail.com", 1234, 1234, 5, this.fecha));
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
    public void queSeCreeReservaCorrectamente() throws DatosInvalidosReserva, EspacioNoDisponible {
        when(repositorioReserva.buscarReserva(anyLong())).thenReturn(null);
        Reserva nuevaReserva = new Reserva(null, this.restauranteInit, "Mateo", "test@mail.com", 46997810, 45238796, 5,
                this.tomorrow);

        servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(), nuevaReserva.getEmail(),
                nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(), nuevaReserva.getCantidadPersonas(),
                nuevaReserva.getFecha());

        verify(repositorioReserva, times(1)).getReservasPorRestaurante(nuevaReserva.getRestaurante().getId());
        verify(repositorioReserva, times(1)).guardar(any());
    }

    @Test
    public void queLanceExcepcionSiQuiereCrearUnaReservaYNoHayEspacioDisponible()
            throws EspacioNoDisponible, DatosInvalidosReserva {
        Restaurante restaurante = new Restaurante(1L, "El Club de la milanesa", 4.0, "Arieta 5000", "restaurant.jpg",
                2);
        Reserva nuevaReserva = new Reserva(10L, restaurante, "Pepe", "test@mail.com", 1234, 1234, 5, this.tomorrow);

        assertThrows(EspacioNoDisponible.class,
                () -> servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(),
                        nuevaReserva.getEmail(), nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(),
                        nuevaReserva.getCantidadPersonas(), nuevaReserva.getFecha()));
        verify(repositorioReserva, never()).guardar(nuevaReserva);
    }

    @Test
    public void queSeActualizeReservaCorrectamente() throws ReservaNoEncontrada {
        Reserva reservaEncontrada = this.reservasMock.get(0);
        reservaEncontrada.setCantidadPersonas(9);
        when(repositorioReserva.buscarReserva(anyLong())).thenReturn(reservaEncontrada);

        servicioReserva.actualizar(reservaEncontrada);

        verify(repositorioReserva, times(1)).actualizar(reservaEncontrada);
    }

    @Test
    public void queLanceExcepcionSiQuiereActualizarUnaReservaQueNoExiste() throws ReservaNoEncontrada {
        Reserva reserva = new Reserva(null, this.restauranteInit, "Mateo", "test@mail.com", 46997810, 45238796, 5,
                this.tomorrow);

        assertThrows(ReservaNoEncontrada.class, () -> servicioReserva.actualizar(reserva));
        verify(repositorioReserva, never()).actualizar(reserva);
    }

    @Test
    public void queSeElimineReservaCorrectamente() throws ReservaNoEncontrada {
        Reserva reserva = this.reservasMock.get(0);
        when(repositorioReserva.buscarReserva(anyLong())).thenReturn(reserva);

        servicioReserva.cancelarReserva(reserva);

        verify(repositorioReserva, times(1)).eliminar(reserva);
    }

    @Test
    public void queLanceExcepcionSiQuiereEliminarUnReservaQueNoExiste() throws ReservaNoEncontrada {
        Reserva reserva = this.reservasMock.get(0);
        when(repositorioReserva.buscarReserva(anyLong())).thenReturn(null);

        assertThrows(ReservaNoEncontrada.class, () -> servicioReserva.cancelarReserva(reserva));
        verify(repositorioReserva, never()).eliminar(reserva);
    }

    @Test
    public void queAlBuscarReservaDelUsuarioDevuelvaUnaListaDeReservas() {

        Reserva reservaMock1 = mock(Reserva.class);
        Reserva reservaMock2 = mock(Reserva.class);
        Reserva reservaMock3 = mock(Reserva.class);

        List<Reserva> listaDeReservas = new ArrayList<>();

        listaDeReservas.add(reservaMock1);
        listaDeReservas.add(reservaMock2);
        listaDeReservas.add(reservaMock3);

        when(repositorioReserva.buscarReservasDelUsuario(anyLong())).thenReturn(listaDeReservas);

        ArrayList<Reserva> listaDevueltaPorElServicio = (ArrayList<Reserva>) servicioReserva
                .buscarReservasDelUsuario(anyLong());

        assertThat(listaDevueltaPorElServicio, containsInAnyOrder(listaDeReservas.toArray()));

    }
}