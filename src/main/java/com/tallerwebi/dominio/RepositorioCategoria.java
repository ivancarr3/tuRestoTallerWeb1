package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCategoria {
    List<Categoria> get();

    Categoria buscarCategoria(Long id);

    void guardar(Categoria categoria);

    void modificar(Categoria categoria);

    void eliminar(Categoria categoria);

    public List<Categoria> obtenerCategorias();
}
