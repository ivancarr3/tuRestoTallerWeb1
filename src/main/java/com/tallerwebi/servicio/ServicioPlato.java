package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;

import java.util.List;

public interface ServicioPlato {

    Plato consultar(Long id) throws PlatoNoEncontrado;
    List<Plato> consultarPlatoPorNombre(String nombre) throws PlatoNoEncontrado;
    List<Plato> consultarPlatoPorPrecio(Double precio) throws PlatoNoEncontrado;
    List<Plato> get() throws NoHayPlatos;
    List<Plato> getPlatosDeRestaurante(Long id) throws NoHayPlatos;
    List<Plato> getPlatosAgrupadosPorCategoria();
    List<Plato> ordenarPorPrecio(String tipoDeOrden) throws NoHayPlatos;
    void crearPlato(Plato plato) throws PlatoExistente;
    void actualizarPlato(Plato plato) throws PlatoNoEncontrado;
    void eliminarPlato(Plato id) throws PlatoNoEncontrado;
    List<Plato> getPlatosPorCategoria(String categoria) throws NoHayPlatos;
}
