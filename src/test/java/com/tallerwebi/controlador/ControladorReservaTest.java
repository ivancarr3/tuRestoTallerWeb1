package com.tallerwebi.controlador;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

		
	
	
	
	
}
