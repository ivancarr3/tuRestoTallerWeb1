package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.excepcion.NoHayCategorias;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioNoActivado;
import com.tallerwebi.servicio.ServicioLogin;

import java.util.Collections;
import java.util.List;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;
	private ServicioCategoria servicioCategoriaMock;

	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		servicioCategoriaMock = mock(ServicioCategoria.class);
		controladorLogin = new ControladorLogin(servicioLoginMock, servicioCategoriaMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente() throws UsuarioNoActivado{
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}

	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome() throws UsuarioNoActivado{
		when(usuarioMock.getRol()).thenReturn("ADMIN");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioMock);
		when(usuarioMock.isActivo()).thenReturn(true);

		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioMock.getRol());
	}

	@Test
	public void loginConUsuarioYPasswordCorrectosPeroUsuarioNoActivadoLanzaExcepcion() throws UsuarioNoActivado {
		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioMock);

		usuarioMock.setActivo(false);
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Esta cuenta no está activada."));
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente, NoHayCategorias{
		List<Long> categoriaIds = Collections.emptyList();

		// ejecucion
		ModelAndView modelAndView = controladorLogin.register(usuarioMock, categoriaIds);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		verify(servicioLoginMock, times(1)).registrar(usuarioMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, NoHayCategorias {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);
		List<Long> categoriaIds = Collections.emptyList();

		// ejecucion
		ModelAndView modelAndView = controladorLogin.register(usuarioMock, categoriaIds);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, NoHayCategorias {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock);
		List<Long> categoriaIds = Collections.emptyList();

		// ejecucion
		ModelAndView modelAndView = controladorLogin.register(usuarioMock, categoriaIds);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario."));
	}

	@Test
	public void nuevoUsuarioDevuelveVistaDeNuevoUsuario(){

		ModelAndView modelAndView = controladorLogin.nuevoUsuario();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));

	}

	@Test
	public void irALoginDevuelveLaVistaDelLogin(){
		ModelAndView modelAndView = controladorLogin.irALogin();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
	}

	@Test
	public void confirmarCuentaConTokenValido() throws NoExisteUsuario {
		String token = "valid_token";

		ModelAndView modelAndView = controladorLogin.confirmarCuenta(token);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("mensaje").toString(), equalToIgnoringCase("Cuenta activada con éxito. Por favor, inicie sesión."));
		verify(servicioLoginMock, times(1)).activarUsuario(token);
	}

	@Test
	public void confirmarCuentaConTokenInvalido() throws NoExisteUsuario {
		String token = "invalid_token";
		doThrow(new NoExisteUsuario()).when(servicioLoginMock).activarUsuario(token);

		ModelAndView modelAndView = controladorLogin.confirmarCuenta(token);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Token inválido"));
		verify(servicioLoginMock, times(1)).activarUsuario(token);
	}

	@Test
	public void logoutDebeInvalidarSesionYRedirigirALogin() {
		when(requestMock.getSession(false)).thenReturn(sessionMock);

		ModelAndView modelAndView = controladorLogin.logout(requestMock);

		verify(sessionMock, times(1)).invalidate();
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
	}

	@Test
	public void logoutSinSesionDebeRedirigirALogin() {
		when(requestMock.getSession(false)).thenReturn(null);

		ModelAndView modelAndView = controladorLogin.logout(requestMock);

		verify(sessionMock, times(0)).invalidate();
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
	}
}
