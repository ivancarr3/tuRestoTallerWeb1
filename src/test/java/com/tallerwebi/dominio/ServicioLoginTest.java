package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        when(repositorioUsuario.buscarUsuario("test@test.com", "pass")).thenReturn(usr);
        when(usr.getEmail()).thenReturn("test@test.com");
        when(usr.getPassword()).thenReturn("pass");

        Usuario usuarioDevuelto = servicioLogin.consultarUsuario("test@test.com", "pass");

        assertThat(usuarioDevuelto.getEmail(), equalToIgnoringCase("test@test.com"));
        assertThat(usuarioDevuelto.getPassword(), equalToIgnoringCase("pass"));
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

        Usuario usr = mock(Usuario.class);

        when(usr.getEmail()).thenReturn("dsfa");
        when(usr.getPassword()).thenReturn("dsfa");

        when(repositorioUsuario.buscarUsuario(anyString(), anyString())).thenReturn(usr);

        try {
            servicioLogin.registrar(usr);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
