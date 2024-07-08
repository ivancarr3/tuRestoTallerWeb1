package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private ServicioUsuarioImpl servicioUsuario;

    private Usuario usuarioMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        usuarioMock = new Usuario(1L, "test@mail.com", "password", "ADMIN", "mateo", "fortuna", new Date());
        this.usuarioMock.setActivo(true);
    }

    @Test
    public void queBuscarUsuarioDevuelvaUsuarioCorrectamente() throws NoExisteUsuario {
        when(repositorioUsuario.buscarUsuario("test@mail.com", "password")).thenReturn(usuarioMock);

        Usuario usuario = servicioUsuario.buscarUsuario("test@mail.com", "password");

        assertThat(usuario, equalTo(usuarioMock));
    }

    @Test
    public void queBuscarUsuarioLanceNoExisteUsuarioCuandoNoSeEncuentraUsuario() {
        when(repositorioUsuario.buscarUsuario("test@mail.com", "password")).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.buscarUsuario("test@mail.com", "password"));
    }

    @Test
    public void queBuscarUsuarioLanceNoExisteUsuarioCuandoUsuarioNoEstaActivo() {
        this.usuarioMock.setActivo(false);
        when(repositorioUsuario.buscarUsuario("test@mail.com", "password")).thenReturn(usuarioMock);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.buscarUsuario("test@mail.com", "password"));
    }

    @Test
    public void queConsultarDevuelvaUsuarioCorrectamente() throws NoExisteUsuario {
        when(repositorioUsuario.consultar(1L)).thenReturn(usuarioMock);

        Usuario usuario = servicioUsuario.consultar(1L);

        assertThat(usuario, equalTo(usuarioMock));
    }

    @Test
    public void queConsultarLanceNoExisteUsuarioCuandoNoSeEncuentraUsuario() {
        when(repositorioUsuario.consultar(1L)).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.consultar(1L));
    }

    @Test
    public void queBuscarDevuelvaUsuarioCorrectamente() throws NoExisteUsuario {
        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(usuarioMock);

        Usuario usuario = servicioUsuario.buscar("test@mail.com");

        assertThat(usuario, equalTo(usuarioMock));
    }

    @Test
    public void queBuscarLanceNoExisteUsuarioCuandoNoSeEncuentraUsuario() {
        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.buscar("test@mail.com"));
    }

    @Test
    public void queGuardarLanceUsuarioExistenteCuandoUsuarioYaExiste() {
        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(usuarioMock);

        assertThrows(UsuarioExistente.class, () -> servicioUsuario.guardar(usuarioMock));
    }

    @Test
    public void queGuardarUsuarioCorrectamente() throws UsuarioExistente {
        when(repositorioUsuario.buscar("test@mail.com")).thenReturn(null);

        servicioUsuario.guardar(usuarioMock);

        verify(repositorioUsuario, times(1)).guardar(usuarioMock);
    }

    @Test
    public void queModificarLanceNoExisteUsuarioCuandoUsuarioNoSeEncuentra() {
        when(repositorioUsuario.consultar(1L)).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.modificar(usuarioMock));
    }

    @Test
    public void queModificarUsuarioCorrectamente() throws NoExisteUsuario {
        when(repositorioUsuario.consultar(1L)).thenReturn(usuarioMock);

        servicioUsuario.modificar(usuarioMock);

        verify(repositorioUsuario, times(1)).modificar(usuarioMock);
    }

    @Test
    public void queEliminarLanceNoExisteUsuarioCuandoUsuarioNoSeEncuentra() {
        when(repositorioUsuario.consultar(1L)).thenReturn(null);

        assertThrows(NoExisteUsuario.class, () -> servicioUsuario.eliminar(usuarioMock));
    }

    @Test
    public void queEliminarUsuarioCorrectamente() throws NoExisteUsuario {
        when(repositorioUsuario.consultar(1L)).thenReturn(usuarioMock);

        servicioUsuario.eliminar(usuarioMock);

        verify(repositorioUsuario, times(1)).eliminar(usuarioMock);
    }
}
