package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPlato {

    Plato buscar(Long id);
    List<Plato> buscarPlatoPorNombre(String nombre);
    List<Plato> buscarPlatoPorPrecio(Integer precio);
    void guardarPlato(Plato plato);
    void modificarPlato(Plato plato);
    void eliminarPlato(Plato plato);
}

