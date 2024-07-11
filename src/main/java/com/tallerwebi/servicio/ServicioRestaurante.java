package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoExisteDireccion;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;

import java.util.List;

public interface ServicioRestaurante {

    Restaurante consultar(Long id) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorNombre(String nombre) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorEstrellas(Double estrellas) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorFiltros(Double estrellas, String tipoDeOrden) throws RestauranteNoEncontrado;
    List<Restaurante> consultarRestaurantePorDireccion(String direccion) throws NoHayRestaurantes;
    List<Restaurante> consultarOrdenPorEstrellas(String tipoDeOrden) throws NoHayRestaurantes;
    List<Restaurante> consultarRestaurantePorEspacio(Integer capacidad) throws NoHayRestaurantes;
    List<Restaurante> get() throws NoHayRestaurantes;
    List<Restaurante> filtrarPorDireccion(String direccion, double radio) throws NoHayRestaurantes, NoExisteDireccion;
    void crearRestaurante(Restaurante restaurante) throws RestauranteExistente;
    void actualizarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado;
    void eliminarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado;
    List<Restaurante> obtenerRestaurantesDeshabilitados();
    List<Restaurante> obtenerRestaurantesHabilitados();
    void habilitarRestaurante(Long id);
    void deshabilitarRestaurante(Long id);
    void eliminarRestaurantePorId(Long id);
    Restaurante consultarPorUsuarioId(Long usuarioId) throws RestauranteNoEncontrado;
}