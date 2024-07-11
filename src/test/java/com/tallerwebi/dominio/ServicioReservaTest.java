package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ServicioReservaTest {

    private static final Long EXISTING_ID = 1L;
    private static final String EXISTING_NAME = "El Club de la milanesa";
    private static final String EXISTING_EMAIL = "test@mail.com";
    private static final int EXISTING_PHONE = 1234;
    private static final int EXISTING_DNI = 1234;
    private static final int EXISTING_PEOPLE = 5;
    private static final String IMAGE = "restaurant.jpg";
    private static final String ADDRESS = "Arieta 5000";
    private static final double RATING = 4.0;
    private static final double LATITUDE = -34.610000;
    private static final double LONGITUDE = -58.400000;
    private static final boolean HABILITADO = true;

    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private ServicioReserva servicioReserva;
    private RepositorioUsuario repositorioUsuario;
    private RepositorioReserva repositorioReserva;
    private ServicioGeocoding servicioGeocoding;
    private Email emailSender;
    private Restaurante restauranteInit;
    private List<Reserva> reservasMock;
    private Date fecha;
    private Date tomorrow;
    private Usuario usuarioInit;

    @BeforeEach
    public void init(){
        initializeDates();
        initializeMocks();
        initializeReservasMock();
        this.restauranteInit = new Restaurante(EXISTING_ID, EXISTING_NAME, RATING, ADDRESS, IMAGE, 7, LATITUDE, LONGITUDE, HABILITADO, new Usuario());
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante, this.servicioReserva, this.servicioGeocoding);
        this.servicioReserva = new ServicioReservaImpl(this.repositorioReserva, this.repositorioRestaurante, this.emailSender, this.repositorioUsuario);
    }

    private void initializeDates() {
        this.fecha = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        this.tomorrow = cal.getTime();
    }

    private void initializeMocks() {
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.repositorioReserva = mock(RepositorioReserva.class);
        this.emailSender = mock(Email.class);
        this.usuarioInit = mock(Usuario.class);
        this.repositorioUsuario = mock(RepositorioUsuario.class);
        this.servicioGeocoding = mock(ServicioGeocoding.class);
    }

    private void initializeReservasMock() {
        this.reservasMock = new ArrayList<>();
        this.reservasMock.add(new Reserva(1L, restauranteInit, "Pepe", EXISTING_EMAIL, EXISTING_PHONE, EXISTING_DNI, EXISTING_PEOPLE, this.fecha, new Usuario(), null));
        this.reservasMock.add(new Reserva(2L, restauranteInit, "Pepe", EXISTING_EMAIL, EXISTING_PHONE, EXISTING_DNI, EXISTING_PEOPLE, this.fecha, new Usuario(), null));
        this.reservasMock.add(new Reserva(3L, restauranteInit, "Pepe", EXISTING_EMAIL, EXISTING_PHONE, EXISTING_DNI, EXISTING_PEOPLE, this.fecha, new Usuario(), null));
    }

    @Test
    public void queSePuedaObtenerUnaReservaPorId() throws ReservaNoEncontrada {
        when(this.repositorioReserva.buscarReserva(EXISTING_ID)).thenReturn(this.reservasMock.get(0));

        Reserva reserva = this.servicioReserva.buscarReserva(EXISTING_ID);
	}

	@Test
	public void queSePuedanObtenerTodasLasReservas() throws NoHayReservas {
		when(this.repositorioReserva.buscarTodasLasReservas()).thenReturn(this.reservasMock);

		List<Reserva> reservas = this.servicioReserva.buscarTodasLasReservas();

		assertThat(reservas.size(), equalTo(3));
	}

    @Test
    public void queLanceExcepcionSiNoEncuentraReserva() throws ReservaNoEncontrada {
        when(this.repositorioReserva.buscarReserva(EXISTING_ID)).thenReturn(null);

        assertThrows(ReservaNoEncontrada.class, () -> servicioReserva.buscarReserva(EXISTING_ID));
    }

	@Test
	public void queLanceExcepcionSiNoExistenReservas() throws NoHayReservas {
		when(this.repositorioReserva.buscarTodasLasReservas()).thenReturn(null);
		assertThrows(NoHayReservas.class, () -> this.servicioReserva.buscarTodasLasReservas());
	}

    @Test
    public void queLanceExcepcionSiQuiereCrearUnaReservaYNoHayEspacioDisponible() throws EspacioNoDisponible, DatosInvalidosReserva {
        Restaurante restaurante = new Restaurante(EXISTING_ID, EXISTING_NAME, RATING, ADDRESS, IMAGE, 2, LATITUDE, LONGITUDE, HABILITADO, new Usuario());
        Reserva nuevaReserva = new Reserva(10L, restaurante, "Pepe", EXISTING_EMAIL, EXISTING_PHONE, EXISTING_DNI, EXISTING_PEOPLE, this.tomorrow, new Usuario(), null);

        assertThrows(EspacioNoDisponible.class, () -> servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(), nuevaReserva.getEmail(), nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(), nuevaReserva.getCantidadPersonas(), nuevaReserva.getFecha(), usuarioInit));

        verify(repositorioReserva, never()).guardar(nuevaReserva);
    }

    @Test
    public void queLanceExcepcionSiQuiereCrearUnaReservaYNoExisteUsuario() throws EspacioNoDisponible, DatosInvalidosReserva {
        Restaurante restaurante = new Restaurante(EXISTING_ID, EXISTING_NAME, RATING, ADDRESS, IMAGE, 2, LATITUDE, LONGITUDE, HABILITADO, new Usuario());
        Reserva nuevaReserva = new Reserva(10L, restaurante, "Pepe", EXISTING_EMAIL, EXISTING_PHONE, EXISTING_DNI, EXISTING_PEOPLE, this.tomorrow, new Usuario(), null);

        assertThrows(NoExisteUsuario.class, () -> servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(), nuevaReserva.getEmail(), nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(), nuevaReserva.getCantidadPersonas(), nuevaReserva.getFecha(), null));

        verify(repositorioReserva, never()).guardar(nuevaReserva);
    }

	@Test
	public void queSeCreeReservaCorrectamente() throws DatosInvalidosReserva, EspacioNoDisponible, NoExisteUsuario, ReservaNoEncontrada {
		when(repositorioReserva.buscarReserva(anyLong())).thenReturn(null);

		Reserva nuevaReserva = new Reserva(null, this.restauranteInit, "Mateo", "test@mail.com", 46997810, 45238796, 5,
				this.tomorrow, new Usuario(), null);

		servicioReserva.crearReserva(nuevaReserva.getRestaurante(), nuevaReserva.getNombre(), nuevaReserva.getEmail(),
				nuevaReserva.getNumeroCelular(), nuevaReserva.getDni(), nuevaReserva.getCantidadPersonas(),
				nuevaReserva.getFecha(), usuarioInit);

		verify(repositorioReserva, times(1)).getReservasPorRestaurante(nuevaReserva.getRestaurante().getId());
		verify(repositorioReserva, times(1)).guardar(any());
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
    public void queSeObtenerEmailsUsuariosPorRestauranteCorrectamente() throws NoHayReservas {
        Long restauranteId = EXISTING_ID;
        List<String> expectedEmails = List.of("email1@example.com", "email2@example.com");
        when(repositorioReserva.buscarEmailDeUsuariosPorRestaurante(restauranteId)).thenReturn(expectedEmails);

        List<String> result = servicioReserva.obtenerEmailsUsuariosPorRestaurante(restauranteId);

        assertThat(result, containsInAnyOrder("email1@example.com", "email2@example.com"));
    }

    @Test
    public void queLanceExcepcionSiNoHayEmails() throws NoHayReservas {
        Long restauranteId = EXISTING_ID;
        List<String> expectedEmails = new ArrayList<>();
        when(repositorioReserva.buscarEmailDeUsuariosPorRestaurante(restauranteId)).thenReturn(expectedEmails);
        assertThrows(NoHayReservas.class, () -> servicioReserva.obtenerEmailsUsuariosPorRestaurante(restauranteId));
    }

	@Test
	public void queSeElimineReservaCorrectamente() throws ReservaNoEncontrada {
		Reserva reserva = this.reservasMock.get(0);
		when(repositorioReserva.buscarReserva(anyLong())).thenReturn(reserva);

		servicioReserva.cancelarReserva(reserva);

		verify(repositorioReserva, times(1)).eliminar(reserva);
	}

	@Test
	public void queAlBuscarReservaDelUsuarioDevuelvaUnaListaDeReservas() throws NoHayReservas {

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

    @Test
    public void queLanceExcepcionNoHayReservasSiNoSeEncuentranReservasDelUsuario() throws NoHayReservas {
        List<Reserva> listaDeReservas = new ArrayList<>();
        when(repositorioReserva.buscarReservasDelUsuario(anyLong())).thenReturn(listaDeReservas);

        assertThrows(NoHayReservas.class, () -> servicioReserva.buscarReservasDelUsuario(anyLong()));
    }

    @Test
    public void queLanceExcepcionGeneralSiNoSeEncuentranReservasDelUsuario() throws NoHayReservas {
        List<Reserva> listaDeReservas = new ArrayList<>();
        when(repositorioReserva.buscarReservasDelUsuario(anyLong())).thenThrow(new RuntimeException("error"));
        assertThrows(Exception.class, () -> servicioReserva.buscarReservasDelUsuario(anyLong()));
    }

    @Test
    public void queBuscarReservasDelRestauranteDevuelvaReservasCorrectamente() throws NoHayReservas {
        when(repositorioReserva.buscarReservasDelRestaurante(EXISTING_ID)).thenReturn(reservasMock);

        List<Reserva> reservas = servicioReserva.buscarReservasDelRestaurante(EXISTING_ID);

        assertThat(reservas, equalTo(reservasMock));
    }

    @Test
    public void queBuscarReservasDelRestauranteLanceNoHayReservasCuandoNoHayReservas() {
        when(repositorioReserva.buscarReservasDelRestaurante(EXISTING_ID)).thenReturn(new ArrayList<>());

        assertThrows(NoHayReservas.class, () -> servicioReserva.buscarReservasDelRestaurante(EXISTING_ID));
    }

    @Test
    public void queBuscarReservasDelRestauranteLanceRuntimeExceptionCuandoOcurreOtroError() {
        when(repositorioReserva.buscarReservasDelRestaurante(EXISTING_ID)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> servicioReserva.buscarReservasDelRestaurante(EXISTING_ID));
    }

    @Test
    public void queBuscarReservasDelUsuarioPasadasDevuelvaReservasCorrectamente() throws NoHayReservasPasadas {
        when(repositorioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID)).thenReturn(reservasMock);

        List<Reserva> reservas = servicioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID);

        assertThat(reservas, equalTo(reservasMock));
    }

    @Test
    public void queBuscarReservasDelUsuarioPasadasLanceNoHayReservasCuandoNoHayReservas() {
        when(repositorioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID)).thenReturn(new ArrayList<>());

        assertThrows(NoHayReservasPasadas.class, () -> servicioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID));
    }

    @Test
    public void queBuscarReservasDelUsuarioPasadasLanceRuntimeExceptionCuandoOcurreOtroError() {
        when(repositorioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> servicioReserva.buscarReservasDelUsuarioPasadas(EXISTING_ID));
    }
}
