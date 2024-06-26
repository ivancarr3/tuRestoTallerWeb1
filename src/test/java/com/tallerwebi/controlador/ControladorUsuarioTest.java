package com.tallerwebi.controlador;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.servicio.ServicioReserva;


public class ControladorUsuarioTest {

	
	private ControladorUsuario controladorUsuario;
	private ServicioReserva servicioReservaMock;
	
//	@BeforeEach
//	public void init() {
//		servicioReservaMock = mock(ServicioReserva.class);
//		this.controladorUsuario = new ControladorUsuario(this.servicioReservaMock);
//	}
//	
//	@Test
//	public void queElUsuarioPuedaVerSusReservas() {
//		
//		List<Reserva> reservasMockeadas = new ArrayList<>();
//		
//		Restaurante restaurante = new Restaurante(1L, "El club de la Milanesa", 2.0, "Santa Maria 3211", "imagen.jpg", 100);
//		Date fecha = new Date();
//		Reserva reserva1 = new Reserva(2L, restaurante, "Gene", "asasd@gmail.com", 1123456789, 39214204, 2, fecha );
//		
//		reservasMockeadas.add(reserva1);
//		
//		when(servicioReservaMock.buscarTodasLasReservas()).thenReturn(reservasMockeadas);
//		
//		ModelAndView modelAndView = controladorUsuario.mostrarMisReservas();
//		
//		assertEquals(modelAndView.getViewName() , "mis_reservas");
//	}
//	
}
