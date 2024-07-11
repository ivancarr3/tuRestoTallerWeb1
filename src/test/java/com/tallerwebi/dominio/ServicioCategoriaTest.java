package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.DemasiadasPreferenciasUsuarioRegistro;
import com.tallerwebi.dominio.excepcion.NoHayCategorias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioCategoriaTest {

    private RepositorioCategoria repositorioCategoria;
    private ServicioCategoriaImpl servicioCategoria;

    @BeforeEach
    public void init() {
        repositorioCategoria = mock(RepositorioCategoria.class);
        servicioCategoria = new ServicioCategoriaImpl(repositorioCategoria);
    }

    @Test
    public void getDevuelveCategorias() throws NoHayCategorias {
        List<Categoria> categorias = Arrays.asList(new Categoria(), new Categoria());
        when(repositorioCategoria.get()).thenReturn(categorias);

        List<Categoria> result = servicioCategoria.get();
        assertEquals(categorias, result);
    }

    @Test
    public void getLanzaExcepcionSiNoHayCategorias() {
        when(repositorioCategoria.get()).thenReturn(null);

        assertThrows(NoHayCategorias.class, () -> servicioCategoria.get());
    }

    @Test
    public void buscarCategoriaDevuelveCategoria() throws NoHayCategorias {
        Categoria categoria = new Categoria();
        when(repositorioCategoria.buscarCategoria(1L)).thenReturn(categoria);

        Categoria result = servicioCategoria.buscarCategoria(1L);
        assertEquals(categoria, result);
    }

    @Test
    public void buscarCategoriaLanzaExcepcionSiNoExisteCategoria() {
        when(repositorioCategoria.buscarCategoria(1L)).thenReturn(null);

        assertThrows(NoHayCategorias.class, () -> servicioCategoria.buscarCategoria(1L));
    }

    @Test
    public void crearCategoriaGuardaCategoria() {
        Categoria categoria = new Categoria();
        servicioCategoria.crearCategoria(categoria);

        verify(repositorioCategoria).guardar(categoria);
    }

    @Test
    public void editarCategoriaModificaCategoria() {
        Categoria categoria = new Categoria();
        servicioCategoria.editarCategoria(categoria);

        verify(repositorioCategoria).modificar(categoria);
    }

    @Test
    public void eliminarCategoriaEliminaCategoria() {
        Categoria categoria = new Categoria();
        servicioCategoria.eliminarCategoria(categoria);

        verify(repositorioCategoria).eliminar(categoria);
    }

    @Test
    public void getCategoriasPorIdsDevuelveCategorias() throws NoHayCategorias, DemasiadasPreferenciasUsuarioRegistro {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<Categoria> categorias = Arrays.asList(new Categoria(), new Categoria());
        when(repositorioCategoria.buscarCategoriasPorListDeIds(ids)).thenReturn(categorias);

        List<Categoria> result = servicioCategoria.getCategoriasPorIds(ids);
        assertEquals(categorias, result);
    }

    @Test
    public void getCategoriasPorIdsLanzaExcepcionSiNoHayCategorias() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(repositorioCategoria.buscarCategoriasPorListDeIds(ids)).thenReturn(null);

        assertThrows(NoHayCategorias.class, () -> servicioCategoria.getCategoriasPorIds(ids));
    }

    @Test
    public void getCategoriasPorIdsLanzaExcepcionSiDemasiadasPreferencias() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        assertThrows(DemasiadasPreferenciasUsuarioRegistro.class, () -> servicioCategoria.getCategoriasPorIds(ids));
    }
}
