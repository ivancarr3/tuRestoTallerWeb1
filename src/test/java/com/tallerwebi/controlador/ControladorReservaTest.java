package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorReservaTest {
	private ControladorReserva controladorReserva;
	private ServicioReserva servicioReservaMock;
	private ServicioRestaurante servicioRestauranteMock;

	@BeforeEach
	public void init() {
		servicioReservaMock = mock(ServicioReserva.class);
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		this.controladorReserva = new ControladorReserva(this.servicioRestauranteMock, this.servicioReservaMock);
	}

	@Test
	public void laReservaSeRealizaExitosamente()
			throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado {
		Restaurante restaurante = new Restaurante(1L, "asd", 2.2, "asd", "asd", 3);

		when(servicioRestauranteMock.consultar(1L)).thenReturn(restaurante);

		doNothing().when(servicioReservaMock).crearReserva(restaurante, "asdfs",
				"sadfasd", 1,
				1, 1, Date.valueOf("2020-10-10"));

		ModelAndView modelAndView = controladorReserva.reservar(1L, "asdfs", "sadfasd", 1,
				1, 1, Date.valueOf("2020-10-10"));

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("reserva_exitosa"));
	}

	@Test
	public void noSeEncontroRestaurante() throws RestauranteNoEncontrado, DatosInvalidosReserva, EspacioNoDisponible {
		doThrow(new RestauranteNoEncontrado()).when(servicioRestauranteMock).consultar(1L);

		ModelAndView modelAndView = controladorReserva.reservar(1L, "asdfs", "sadfasd", 1,
				1, 1, Date.valueOf("2020-10-10"));

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("No se encontró el restaurante."));

	}

	@Test
	public void espacioNoDisponible() throws RestauranteNoEncontrado, EspacioNoDisponible, DatosInvalidosReserva {
		Restaurante restaurante = new Restaurante(1L, "asd", 2.2, "asd", "asd", 3);

		when(servicioRestauranteMock.consultar(1L)).thenReturn(restaurante);

		doThrow(new EspacioNoDisponible()).when(servicioReservaMock).crearReserva(restaurante, "dsa", "asd",
				2, 2,
				2, Date.valueOf("2020-10-10"));

		ModelAndView modelAndView = controladorReserva.reservar(1L, "dsa", "asd", 2,
				2, 2, Date.valueOf("2020-10-10"));

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("No hay suficiente espacio disponible en el restaurante."));

	}

	@Test
	public void datosInvalidos() throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado {
		Restaurante restaurante = new Restaurante(1L, "asd", 2.2, "asd", "asd", 3);

		when(servicioRestauranteMock.consultar(1L)).thenReturn(restaurante);

		doThrow(new DatosInvalidosReserva()).when(servicioReservaMock).crearReserva(restaurante, "dsa", "asd",
				2, 2,
				2, Date.valueOf("2020-10-10"));

		ModelAndView modelAndView = controladorReserva.reservar(1L, "dsa", "asd", 2,
				2, 2, Date.valueOf("2020-10-10"));

		assertThat(modelAndView.getModel().get("error").toString(),
				equalToIgnoringCase("Datos ingresados invalidos para la reserva"));

	}

}
