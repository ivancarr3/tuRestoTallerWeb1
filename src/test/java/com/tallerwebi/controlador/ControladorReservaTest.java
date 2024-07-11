package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Date;
import com.tallerwebi.dominio.Reserva;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.servicio.ServicioMercadoPago;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControladorReservaTest {

	private ControladorReserva controladorReserva;
	private ServicioReserva servicioReservaMock;
	private ServicioMercadoPago servicioMercadoPago;
	private ServicioRestaurante servicioRestauranteMock;
	private ServicioUsuario servicioUsuario;
	private HttpServletRequest request;
	private HttpSession session;
	private Usuario usuarioInit;
	private RedirectAttributes redirectAttributes;

	@Captor
	private ArgumentCaptor<String> stringCaptor;

	@Captor
	private ArgumentCaptor<Object> objectCaptor;

	@BeforeEach
	public void init() {
		servicioReservaMock = mock(ServicioReserva.class);
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		servicioMercadoPago = mock(ServicioMercadoPago.class);
		redirectAttributes = mock(RedirectAttributes.class);
		MockitoAnnotations.openMocks(this);

		//this.controladorReserva = new ControladorReserva(this.servicioRestauranteMock, this.servicioReservaMock,
		servicioUsuario = mock(ServicioUsuario.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		usuarioInit = mock(Usuario.class);
		this.controladorReserva = new ControladorReserva(this.servicioUsuario, this.servicioRestauranteMock, this.servicioReservaMock,
				this.servicioMercadoPago);

	}

	@Test
	public void laReservaSeRealizaExitosamente()
			throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado, NoExisteUsuario, ReservaNoEncontrada {
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
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("email")).thenReturn("test@test.com");
		when(servicioUsuario.buscar("test@test.com")).thenReturn(usuarioInit);

		Reserva reserva = new Reserva(null, restaurante, "nombre", "test@test.com", 1, 1, 1, datosReserva.getFechaForm(), new Usuario(), "");
		when(servicioReservaMock.crearReserva(restaurante, datosReserva.getNombreForm(), datosReserva.getEmailForm(), datosReserva.getNumForm(), datosReserva.getDniForm(), datosReserva.getCantPersonas(), datosReserva.getFechaForm(), this.usuarioInit))
				.thenReturn(reserva);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("reserva_exitosa"));
	}

	@Test
	public void errorDeServidorAlIntentarReservar() throws RestauranteNoEncontrado, EspacioNoDisponible, MPException, MPApiException, DatosInvalidosReserva {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("Nombre");
		datosReserva.setEmailForm("email@example.com");
		datosReserva.setNumForm(123456789);
		datosReserva.setDniForm(12345678);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(servicioRestauranteMock.consultar(anyLong())).thenThrow(new RuntimeException("error"));
		when(request.getSession(false)).thenReturn(session);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "Error del servidor: error");
	}

	@Test
	public void alEntrarAReservarDevuelveLaVistaCorrecta() {

		ModelAndView modelAndView = controladorReserva.getRequest();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
	}

	@Test
	public void noSeEncontroRestaurante() throws RestauranteNoEncontrado, DatosInvalidosReserva, EspacioNoDisponible, MPException, MPApiException {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test@test.com");
		datosReserva.setNumForm(1);
		datosReserva.setDniForm(1);
		datosReserva.setCantPersonas(1);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		doThrow(new RestauranteNoEncontrado()).when(servicioRestauranteMock).consultar(1L);
		when(request.getSession(false)).thenReturn(session);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "No se encontró el restaurante.");
	}

	@Test
	public void espacioNoDisponible() throws RestauranteNoEncontrado, EspacioNoDisponible, DatosInvalidosReserva, NoExisteUsuario, ReservaNoEncontrada {
		Restaurante restaurante = new Restaurante(1L, "nombre", 2.2, "direccion", "imagen", 3, -34.610000, -58.400000);

		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("mateo.fortu@gmail.com");
		datosReserva.setNumForm(2);
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(120);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(servicioRestauranteMock.consultar(datosReserva.getIdRestaurante())).thenReturn(restaurante);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("email")).thenReturn("test@test.com");
		when(servicioUsuario.buscar("test@test.com")).thenReturn(usuarioInit);

		doThrow(new EspacioNoDisponible()).when(servicioReservaMock).crearReserva(restaurante, datosReserva.getNombreForm(),
				datosReserva.getEmailForm(), datosReserva.getNumForm(), datosReserva.getDniForm(), datosReserva.getCantPersonas(), datosReserva.getFechaForm(), usuarioInit);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "No hay suficiente espacio disponible en el restaurante.");
	}

	@Test
	public void datosInvalidos() throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test@mail.com");
		datosReserva.setNumForm(null); // Dato inválido
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(request.getSession(false)).thenReturn(session);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "Datos ingresados invalidos para la reserva");
	}

	@Test
	public void queLanceExcepcionSiQuiereReservarConFechaAnterior() throws EspacioNoDisponible, FechaAnterior, DatosInvalidosReserva, RestauranteNoEncontrado, EmailInvalido {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test");
		datosReserva.setNumForm(1234);
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() - 86400000)); // fecha futura

		when(request.getSession(false)).thenReturn(session);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "La fecha de la reserva no puede ser anterior a la de hoy.");
	}

	@Test
	public void queLanceExcepcionSiQuiereReservarConMailInvalido() throws EspacioNoDisponible, DatosInvalidosReserva, RestauranteNoEncontrado, EmailInvalido {
		DatosReserva datosReserva = new DatosReserva();
		datosReserva.setIdRestaurante(1L);
		datosReserva.setNombreForm("nombre");
		datosReserva.setEmailForm("test");
		datosReserva.setNumForm(1234);
		datosReserva.setDniForm(2);
		datosReserva.setCantPersonas(2);
		datosReserva.setFechaForm(new Date(System.currentTimeMillis() + 86400000)); // fecha futura

		when(request.getSession(false)).thenReturn(session);

		ModelAndView modelAndView = controladorReserva.reservar(datosReserva, this.request, this.redirectAttributes);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/restaurante/1"));
		verify(redirectAttributes).addFlashAttribute("errorForm", "El email ingresado no es válido.");
	}
}
