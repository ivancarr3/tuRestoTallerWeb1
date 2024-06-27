package com.tallerwebi.dominio;

public interface RepositorioCategoria {
    Categoria buscarCategoria(Long id);
    void guardar(Categoria categoria);
}
