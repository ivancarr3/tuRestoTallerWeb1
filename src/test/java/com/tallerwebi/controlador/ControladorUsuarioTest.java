package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioUsuario;

public class ControladorUsuarioTest {

	private ControladorUsuario controladorUsuario;
	private ServicioReserva servicioReservaMock;
	private ServicioUsuario servicioUsuarioMock;
	private HttpServletRequest request;
	private HttpSession session;

	@BeforeEach
	public void init() {
		servicioReservaMock = mock(ServicioReserva.class);
		servicioUsuarioMock = mock(ServicioUsuario.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		this.controladorUsuario = new ControladorUsuario(this.servicioReservaMock, this.servicioUsuarioMock);

		// Configurar el mock para que devuelva el session mock cuando se llame a getSession
		when(request.getSession(false)).thenReturn(session);
		// Configurar el mock de la sesión para que devuelva un email cuando se llame a getAttribute
		when(session.getAttribute("email")).thenReturn("test@example.com");
	}

	@Test
	public void elUsuarioCargaSuPerfil() {
		ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario_perfil"));
	}
}
