package com.tallerwebi.dominio.excepcion;

public class RestauranteNoEncontrado extends Exception {
    public RestauranteNoEncontrado() {
        super("No se encontró el restaurante.");
    }
}
