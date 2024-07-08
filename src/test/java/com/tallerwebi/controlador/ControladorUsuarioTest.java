package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioUsuario;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("test@example.com");
    }

    @Test
    public void elUsuarioCargaSuPerfil() throws NoExisteUsuario, NoHayReservas {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(usuarioMock);
        List<Reserva> reservasMock = new ArrayList<Reserva>();
        reservasMock.add(new Reserva());
        reservasMock.add(new Reserva());
        when(servicioReservaMock.buscarReservasDelUsuarioPasadas(usuarioMock.getId())).thenReturn(reservasMock);
        when(servicioReservaMock.buscarReservasDelUsuario(usuarioMock.getId())).thenReturn(reservasMock);
        ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario_perfil"));
        Integer cantidadReservas = (Integer) modelAndView.getModel().get("cantidadReservas");
        List<Reserva> reservas = (List<Reserva>) modelAndView.getModel().get("reservas");
        System.out.println(modelAndView);
        assertEquals(2, cantidadReservas);
        assertEquals(2, reservas.size());
    }

    @Test
    public void queLanceExcepcionAlCargarUsuarioPerfilSinReservas() throws NoHayReservas, NoExisteUsuario {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(usuarioMock);
        when(servicioReservaMock.buscarReservasDelUsuarioPasadas(usuarioMock.getId())).thenReturn(new ArrayList<>());
        ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("No tienes próximas reservas."));
    }

    @Test
    public void queLanceExcepcionAlCargarUsuarioSiNoEncuentraUsuario() throws NoHayReservas, NoExisteUsuario {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(null);
        when(servicioReservaMock.buscarReservasDelUsuarioPasadas(usuarioMock.getId())).thenReturn(new ArrayList<>());
        ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario no encontrado."));
    }

    @Test
    public void queLanceExcepcionGeneralSiHayErrorDelServidorAlCargarUsuario() throws NoHayReservas, NoExisteUsuario {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(usuarioMock);
        when(servicioReservaMock.buscarReservasDelUsuarioPasadas(usuarioMock.getId())).thenThrow(new RuntimeException("error"));
        ModelAndView modelAndView = controladorUsuario.cargarUsuarioPerfil(this.request);

        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error del servidor: error"));
    }

    @Test
    public void historialReservas_conReservasPasadas_devuelveVistaConReservas() throws NoExisteUsuario, NoHayReservas {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(usuarioMock);

        List<Reserva> reservasMock = Arrays.asList(new Reserva(), new Reserva());
        when(servicioReservaMock.buscarReservasDelUsuarioPasadas(1L)).thenReturn(reservasMock);

        ModelAndView modelAndView = controladorUsuario.historialReservas(request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("historial_reservas_usuario"));
        assertThat(((List<Reserva>) modelAndView.getModel().get("reservas")), equalTo(reservasMock));
        assertThat((Usuario) modelAndView.getModel().get("user"), equalTo(usuarioMock));
    }

    @Test
    public void historialReservas_sinReservasPasadas_lanzaExcepcion() throws NoExisteUsuario, NoHayReservas {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        when(servicioUsuarioMock.buscar("test@example.com")).thenReturn(usuarioMock);

        doThrow(new NoHayReservas()).when(servicioReservaMock).buscarReservasDelUsuarioPasadas(1L);

        ModelAndView modelAndView = controladorUsuario.historialReservas(request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("historial_reservas_usuario"));
        assertThat((String) modelAndView.getModel().get("error"), equalTo("Todavía no has asistido a ninguna reserva."));
    }

    @Test
    public void historialReservas_usuarioNoEncontrado_lanzaExcepcion() throws NoExisteUsuario, NoHayReservas {
        doThrow(new NoExisteUsuario()).when(servicioUsuarioMock).buscar("test@example.com");

        ModelAndView modelAndView = controladorUsuario.historialReservas(request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("historial_reservas_usuario"));
        assertThat((String) modelAndView.getModel().get("error"), equalTo("Usuario no encontrado."));
    }

    @Test
    public void historialReservas_errorServidor_lanzaExcepcion() throws NoExisteUsuario, NoHayReservas {
        doThrow(new RuntimeException("error")).when(servicioUsuarioMock).buscar("test@example.com");

        ModelAndView modelAndView = controladorUsuario.historialReservas(request);

        assertThat(modelAndView.getModel().get("error").toString(), equalTo("Error del servidor: error"));
    }

    @Test
    public void cancelarReserva_reservaEncontrada_redirigeAHome() throws ReservaNoEncontrada {
        Long id_reserva = 1L;
        Reserva reservaMock = new Reserva();
        when(servicioReservaMock.buscarReserva(id_reserva)).thenReturn(reservaMock);

        ModelAndView modelAndView = controladorUsuario.cancelarReserva(request, id_reserva);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
        verify(servicioReservaMock).cancelarReserva(reservaMock);
    }

    @Test
    public void cancelarReserva_reservaNoEncontrada_lanzaExcepcion() throws ReservaNoEncontrada {
        Long id_reserva = 1L;
        doThrow(new ReservaNoEncontrada()).when(servicioReservaMock).buscarReserva(id_reserva);

        ModelAndView modelAndView = controladorUsuario.cancelarReserva(request, id_reserva);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario_perfil"));
        assertThat((String) modelAndView.getModel().get("error"), equalTo("No tienes próximas reservas."));
    }

    @Test
    public void cancelarReserva_errorServidor_lanzaExcepcion() throws ReservaNoEncontrada {
        Long id_reserva = 1L;
        doThrow(new RuntimeException("error")).when(servicioReservaMock).buscarReserva(id_reserva);

        ModelAndView modelAndView = controladorUsuario.cancelarReserva(request, id_reserva);

        assertThat(modelAndView.getModel().get("error").toString(), equalTo("Error del servidor: error"));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario_perfil"));
    }
}
