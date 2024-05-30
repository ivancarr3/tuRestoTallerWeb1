package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPlato {

    Plato buscar(Long id);
    List<Plato> buscarPlatoPorNombre(String nombre);
    List<Plato> buscarPlatoPorPrecio(Double precio);
    List<Plato> get();
    List<Plato> getPlatosDeRestaurante(Long restauranteId);
    List<Plato> ordenarPorPrecio(String tipoDeOrden);
    List<Plato> getPlatosAgrupadosPorCategoria();
    void guardarPlato(Plato plato);
    void modificarPlato(Plato plato);
    void eliminarPlato(Plato plato);
}

