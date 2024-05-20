package com.tallerwebi.dominio.excepcion;

public class RestauranteExistente extends Exception {
    public RestauranteExistente() {
        super("Restaurante existente.");
    }
}
