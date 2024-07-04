package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCategoria {
    Categoria buscarCategoria(Long id);

    void guardar(Categoria categoria);

    public List<Categoria> obtenCategorias();
}
