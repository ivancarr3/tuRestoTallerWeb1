package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.excepcion.NoHayCategorias;

import java.util.List;

public interface ServicioCategoria {
    Categoria buscarCategoria(Long id) throws NoHayCategorias;

    List<Categoria> get() throws NoHayCategorias;

    void crearCategoria(Categoria categoria);

    void editarCategoria(Categoria categoria);

    void eliminarCategoria(Categoria categoria);

    public List<Categoria> obtenerTodasLasCategorias();

    public Categoria getCategoriaDePlato(Long id);
}
