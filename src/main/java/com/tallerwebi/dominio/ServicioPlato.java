package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;

import java.util.List;

public interface ServicioPlato {

    Plato consultar(Long id) throws PlatoNoEncontrado;
    List<Plato> consultarPlatoPorNombre(String nombre) throws PlatoNoEncontrado;
    List<Plato> consultarPlatoPorPrecio(Integer precio) throws PlatoNoEncontrado;
    void crearPlato(Plato plato) throws PlatoExistente;
    void actualizarPlato(Plato plato) throws PlatoNoEncontrado;
    void eliminarPlato(Plato plato) throws PlatoNoEncontrado;
}
