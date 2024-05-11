package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRestaurante {

    List<Restaurante> buscarPorDireccion(String direccion);
    List<Restaurante> buscarPorNombre(String nombre);
    List<Restaurante> buscarPorEstrellas(Double estrellas);
    List<Restaurante> ordenarPorEstrellas(String tipoDeOrden);
    Restaurante buscar(Long id);
    List<Restaurante> get();
    void guardar(Restaurante restaurante);
    void actualizar(Restaurante restaurante);
    void eliminar(Restaurante restaurante);
}

