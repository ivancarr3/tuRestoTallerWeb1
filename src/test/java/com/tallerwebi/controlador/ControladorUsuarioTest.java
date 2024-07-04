package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;

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

	@BeforeEach
	public void init() {
		servicioReservaMock = mock(ServicioReserva.class);
		servicioUsuarioMock = mock(ServicioUsuario.class);
		request = mock(HttpServletRequest.class);
		this.controladorUsuario = new ControladorUsuario(this.servicioReservaMock, this.servicioUsuarioMock);
	}

	@Test
	public void elUsuarioCargaSuPerfil() {
		ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario_perfil"));
	}
}
