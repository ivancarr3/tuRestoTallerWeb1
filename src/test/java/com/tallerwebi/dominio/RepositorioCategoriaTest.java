package com.tallerwebi.dominio;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
@Transactional
public class RepositorioCategoriaTest {

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @BeforeEach
    public void init() {
        Categoria categoria1 = new Categoria(null, "Ensaladas", "img");
        Categoria categoria2 = new Categoria(null, "Hamburguesas", "img");
        Categoria categoria3 = new Categoria(null, "Milanesas", "img");

        repositorioCategoria.guardar(categoria1);
        repositorioCategoria.guardar(categoria2);
        repositorioCategoria.guardar(categoria3);
    }

    @Test
    public void queDevuelvaTodasLasCategorias() {
        List<Categoria> result = repositorioCategoria.get();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void queBusqueCategoriaPorId() {
        Categoria categoria = repositorioCategoria.get().get(1);
        Categoria result = repositorioCategoria.buscarCategoria(categoria.getId());
        assertNotNull(result);
        assertEquals("Hamburguesas", result.getDescripcion());
    }

    @Test
    public void queGuardeCategoriaCorrectamente() {
        Categoria nuevaCategoria = new Categoria(null, "Sopas", "img");
        repositorioCategoria.guardar(nuevaCategoria);
        List<Categoria> result = repositorioCategoria.get();
        assertEquals(4, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getDescripcion().equals("Sopas")));
    }

    @Test
    public void queModifiqueCategoriaCorrectamente() {
        Categoria categoria = repositorioCategoria.get().get(0);
        categoria.setDescripcion("Ensaladas Variadas");
        repositorioCategoria.modificar(categoria);
        Categoria categoriaModificada = repositorioCategoria.buscarCategoria(categoria.getId());
        assertEquals("Ensaladas Variadas", categoriaModificada.getDescripcion());
    }

    @Test
    public void queElimineCategoriaCorrectamente() {
        Categoria categoriaAEliminar = repositorioCategoria.get().get(0);
        Long id = categoriaAEliminar.getId();
        repositorioCategoria.eliminar(categoriaAEliminar);
        Categoria categoria = repositorioCategoria.buscarCategoria(id);
        assertNull(categoria);
    }
}
