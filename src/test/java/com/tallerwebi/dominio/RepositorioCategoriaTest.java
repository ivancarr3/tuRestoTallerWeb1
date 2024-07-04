package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class RepositorioCategoriaTest {

    private RepositorioCategoria repositorioCategoria;
    private List<Categoria> categoriasMock;

    @BeforeEach
    public void init(){
        this.repositorioCategoria = mock(RepositorioCategoria.class);
        this.categoriasMock = new ArrayList<>();
        this.categoriasMock.add(new Categoria(1L, "Ensaladas"));
        this.categoriasMock.add(new Categoria(2L, "Hamburguesas"));
        this.categoriasMock.add(new Categoria(3L, "Milanesas"));
    }




}
