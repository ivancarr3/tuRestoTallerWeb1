package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.excepcion.UsuarioNoActivado;

public class ServicioLoginTest {

    RepositorioUsuario repositorioUsuario;
    ServicioLoginImpl servicioLogin;
    Email emailSender;

    @BeforeEach
    public void init() {
        repositorioUsuario = mock(RepositorioUsuario.class);
        emailSender = mock(Email.class);
        servicioLogin = new ServicioLoginImpl(repositorioUsuario, emailSender);
    }

    @Test
    public void consultarUsuarioDevuelveUnUsuario() throws UsuarioNoActivado {

        Usuario usr = mock(Usuario.class);
        usr.setPassword("pass");
        usr.setActivo(true);

        when(repositorioUsuario.buscarUsuario("test@test.com", "pass")).thenReturn(usr);
        when(usr.getEmail()).thenReturn("test@test.com");
        when(usr.getPassword()).thenReturn("pass");
        when(usr.isActivo()).thenReturn(true);

        Usuario usuarioDevuelto = servicioLogin.consultarUsuario("test@test.com", "pass");

        assertThat(usuarioDevuelto.getEmail(), equalToIgnoringCase("test@test.com"));
        assertThat(usuarioDevuelto.getPassword(), equalToIgnoringCase("pass"));
    }

    @Test
    public void consultarUsuarioDevuelveNullSiNoSeEncuentraUsuario() throws UsuarioNoActivado {
        when(repositorioUsuario.buscarUsuario("test@test.com", "pass")).thenReturn(null);

        Usuario usuarioDevuelto = servicioLogin.consultarUsuario("test@test.com", "pass");

        assertThat(usuarioDevuelto, nullValue());
    }

    @Test
    public void queSePuedaActivarUsuario() throws NoExisteUsuario {
        Usuario usuarioMock = mock(Usuario.class);
        when(repositorioUsuario.buscarPorToken("token")).thenReturn(usuarioMock);

        servicioLogin.activarUsuario("token");

        verify(usuarioMock).setActivo(true);

        verify(repositorioUsuario).modificar(usuarioMock);
    }

    @Test
    public void queLanceExcepcionSiNoSeEcuentraUsuarioParaActivar() throws NoExisteUsuario {
        when(repositorioUsuario.buscarPorToken("token")).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioLogin.activarUsuario("token"));
    }

    @Test
    public void registrarRegistraExitosamenteUnUsuario() {

        Usuario usr = mock(Usuario.class);

        when(usr.getEmail()).thenReturn("dsfa");
        when(usr.getPassword()).thenReturn("dsfa");

        when(repositorioUsuario.buscarUsuario(anyString(), anyString())).thenReturn(null);

        try {
            servicioLogin.registrar(usr);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void registrarEncuentraQueElUsuarioYaExiste() {
        Usuario usr = new Usuario();
        usr.setEmail("dsfa");
        usr.setPassword("dsfa");

        when(repositorioUsuario.buscar("dsfa")).thenReturn(usr);

        assertThrows(UsuarioExistente.class, () -> {
            servicioLogin.registrar(usr);
        });
    }
}
