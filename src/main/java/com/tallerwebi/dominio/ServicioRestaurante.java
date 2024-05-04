package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;

import java.util.List;

public interface ServicioRestaurante {

    Restaurante consultar(Long id) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorNombre(String nombre) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorEstrellas(Integer estrellas) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorDireccion(String direccion) throws RestauranteNoEncontrado;
    void crearRestaurante(Restaurante restaurante) throws RestauranteExistente;
    void actualizarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado;
    void eliminarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado;
}
