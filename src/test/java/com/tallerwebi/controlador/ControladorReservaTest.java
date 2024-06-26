package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tallerwebi.dominio.Reserva;
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
	public void reservaExistosa()
			throws RestauranteNoEncontrado, EspacioNoDisponible, MPException, MPApiException, DatosInvalidosReserva {

		Restaurante restoMock = mock(Restaurante.class);
		Reserva reservaMock = mock(Reserva.class);

		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("Nombre");
		datosReserva.setEmailForm("email@example.com");
		datosReserva.setNumForm(123456789);
		datosReserva.setDniForm(12345678);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000));

		when(servicioRestauranteMock.consultar(anyLong())).thenReturn(restoMock);
		when(servicioReservaMock.crearReserva(restoMock, datosReserva.getNombreForm(),
				datosReserva.getEmailForm(), datosReserva.getNumForm(), datosReserva.getDniForm(),
				datosReserva.getCantPersonas(), datosReserva.getFechaForm())).thenReturn(reservaMock);

		when(servicioMercadoPago.armarPago(restoMock, reservaMock, 5000)).thenReturn("12345");

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("reserva_exitosa"));

		assertThat((String) modelAndView.getModel().get("urlpago"),
				equalToIgnoringCase("https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=12345"));
	}

	@Test
	public void errorDeServidorAlIntentarReservar()
			throws RestauranteNoEncontrado, EspacioNoDisponible, MPException, MPApiException, DatosInvalidosReserva {
		Restaurante restoMock = mock(Restaurante.class);
		Reserva reservaMock = mock(Reserva.class);

		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("Nombre");
		datosReserva.setEmailForm("email@example.com");
		datosReserva.setNumForm(123456789);
		datosReserva.setDniForm(12345678);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000));

		when(servicioRestauranteMock.consultar(anyLong())).thenThrow(new RuntimeException("error"));

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("errReserva"));

		assertThat((String) modelAndView.getModel().get("error"),
				equalToIgnoringCase("Error del servidor: error"));
	}

	@Test
	public void alEntrarAReservarDevuelveLaVistaCorrecta() {

		ModelMap modelo = mock(ModelMap.class);

		ModelAndView modelAndView = controladorReserva.getRequest(modelo);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
	}

	@Test
	public void noSeEncontroRestaurante()
			throws RestauranteNoEncontrado, DatosInvalidosReserva, EspacioNoDisponible, MPException, MPApiException {
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
	public void espacioNoDisponible()
			throws RestauranteNoEncontrado, EspacioNoDisponible, DatosInvalidosReserva, MPException, MPApiException {
		Restaurante restaurante = new Restaurante(1L, "nombre", 2.2, "direccion", "imagen", 3);
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
	public void datosInvalidos()
			throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado, MPException, MPApiException {
		Restaurante restaurante = new Restaurante(1L, "nombre", 2.2, "direccion", "imagen", 3);
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
