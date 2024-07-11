package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.excepcion.DemasiadasPreferenciasUsuarioRegistro;
import com.tallerwebi.dominio.excepcion.NoHayCategorias;

import java.util.List;

public interface ServicioCategoria {
    Categoria buscarCategoria(Long id) throws NoHayCategorias;
    List<Categoria> get() throws NoHayCategorias;
    List<Categoria> getCategoriasPorIds(List<Long> ids) throws NoHayCategorias, DemasiadasPreferenciasUsuarioRegistro;
    void crearCategoria(Categoria categoria);
    void editarCategoria(Categoria categoria);
    void eliminarCategoria(Categoria categoria);
    Categoria getCategoriaDePlato(Long id);
}
