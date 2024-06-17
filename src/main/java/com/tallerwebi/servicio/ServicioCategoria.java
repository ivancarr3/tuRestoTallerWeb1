package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Categoria;

import java.util.List;

public interface ServicioCategoria {

    Categoria getCategoriaDePlato(Long id);
    List<Categoria> obtenerTodasLasCategorias();
}
