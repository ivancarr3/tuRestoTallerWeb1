package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
@Transactional
public class RepositorioUsuarioTest {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    public void setUp() {
        usuario1 = new Usuario(null, "test1@test.com", "password1", "USER", "John", "Doe", new Date());
        usuario2 = new Usuario(null, "test2@test.com", "password2", "USER", "Jane", "Doe", new Date());
        repositorioUsuario.guardar(usuario1);
        repositorioUsuario.guardar(usuario2);
    }

    @Test
    public void queBusqueUsuarioPorEmailYPassword() {
        Usuario usuario = repositorioUsuario.buscarUsuario("test1@test.com", "password1");
        assertNotNull(usuario);
        assertEquals("test1@test.com", usuario.getEmail());
    }

    @Test
    public void queNoEncuentreUsuarioConEmailYPasswordIncorrectos() {
        Usuario usuario = repositorioUsuario.buscarUsuario("test1@test.com", "wrongpassword");
        assertNull(usuario);
    }

    @Test
    public void queBusqueUsuarioPorEmail() {
        Usuario usuario = repositorioUsuario.buscar("test2@test.com");
        assertNotNull(usuario);
        assertEquals("test2@test.com", usuario.getEmail());
    }

    @Test
    public void queNoEncuentreUsuarioConEmailIncorrecto() {
        Usuario usuario = repositorioUsuario.buscar("wrong@test.com");
        assertNull(usuario);
    }

    @Test
    public void queBusqueUsuarioPorId() {
        Usuario usuario = repositorioUsuario.consultar(usuario1.getId());
        assertNotNull(usuario);
        assertEquals(usuario1.getId(), usuario.getId());
    }

    @Test
    public void queNoEncuentreUsuarioConIdIncorrecto() {
        Usuario usuario = repositorioUsuario.consultar(999L);
        assertNull(usuario);
    }

    @Test
    public void queActualiceUsuarioCorrectamente() {
        usuario1.setNombre("UpdatedName");
        repositorioUsuario.modificar(usuario1);
        Usuario usuarioActualizado = repositorioUsuario.consultar(usuario1.getId());
        assertNotNull(usuarioActualizado);
        assertEquals("UpdatedName", usuarioActualizado.getNombre());
    }

    @Test
    public void queElimineUsuarioCorrectamente() {
        repositorioUsuario.eliminar(usuario1);
        Usuario usuarioEliminado = repositorioUsuario.consultar(usuario1.getId());
        assertNull(usuarioEliminado);
    }

    @Test
    public void queBusqueUsuarioPorToken() {
        usuario1.setConfirmationToken("token123");
        repositorioUsuario.modificar(usuario1);
        Usuario usuario = repositorioUsuario.buscarPorToken("token123");
        assertNotNull(usuario);
        assertEquals("token123", usuario.getConfirmationToken());
    }

    @Test
    public void queNoEncuentreUsuarioConTokenIncorrecto() {
        Usuario usuario = repositorioUsuario.buscarPorToken("wrongtoken");
        assertNull(usuario);
    }
}
