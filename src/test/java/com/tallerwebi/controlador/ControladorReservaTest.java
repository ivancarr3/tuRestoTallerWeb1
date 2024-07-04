package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import com.tallerwebi.dominio.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioMercadoPago;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorReservaTest {
	private ControladorReserva controladorReserva;
	private ServicioReserva servicioReservaMock;
	private ServicioMercadoPago servicioMercadoPago;
	private ServicioRestaurante servicioRestauranteMock;

	@BeforeEach
	public void init() {
		servicioReservaMock = mock(ServicioReserva.class);
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		servicioMercadoPago = mock(ServicioMercadoPago.class);
		this.controladorReserva = new ControladorReserva(this.servicioRestauranteMock, this.servicioReservaMock,
				this.servicioMercadoPago);
	}

	@Test
	public void laReservaSeRealizaExitosamente()
			throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado {
		Restaurante restaurante = new Restaurante(1L, "restaurante", 2.2, "direccion", "imagen", 3, -34.610000, -58.400000);
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("mateo");
		datosReserva.setEmailForm("mateo.fortu@gmail.com");
		datosReserva.setNumForm(4523434);
		datosReserva.setDniForm(543534);
		datosReserva.setCantPersonas(1);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(servicioRestauranteMock.consultar(1L)).thenReturn(restaurante);

		Reserva reserva = new Reserva(null, restaurante, "nombre", "test@test.com", 1, 1, 1, datosReserva.getFechaForm());
		when(servicioReservaMock.crearReserva(restaurante, "nombre", "test@test.com", 1, 1, 1, datosReserva.getFechaForm()))
				.thenReturn(reserva);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("reserva_exitosa"));
	}

	@Test
	public void noSeEncontroRestaurante() throws RestauranteNoEncontrado, DatosInvalidosReserva, EspacioNoDisponible {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test@test.com");
		datosReserva.setNumForm(1);
		datosReserva.setDniForm(1);
		datosReserva.setCantPersonas(1);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		doThrow(new RestauranteNoEncontrado()).when(servicioRestauranteMock).consultar(1L);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("No se encontró el restaurante."));
	}

	@Test
	public void espacioNoDisponible() throws RestauranteNoEncontrado, EspacioNoDisponible, DatosInvalidosReserva {
		Restaurante restaurante = new Restaurante(1L, "nombre", 2.2, "direccion", "imagen", 3, -34.610000, -58.400000);
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test@test.com");
		datosReserva.setNumForm(2);
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(servicioRestauranteMock.consultar(1L)).thenReturn(restaurante);

		doThrow(new EspacioNoDisponible()).when(servicioReservaMock).crearReserva(restaurante, "nombre",
				"test@test.com", 2, 2, 2, datosReserva.getFechaForm());

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("No hay suficiente espacio disponible en el restaurante."));
	}

	@Test
	public void datosInvalidos() throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado {
		Restaurante restaurante = new Restaurante(1L, "nombre", 2.2, "direccion", "imagen", 3, -34.610000, -58.400000);
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test@mail.com");
		datosReserva.setNumForm(null);
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("Datos ingresados invalidos para la reserva"));
	}
}
