package com.tallerwebi.dominio.excepcion;

public class NoHayCategorias extends Exception{
    public NoHayCategorias() {
        super("No hay categorías disponibles.");
    }
}
