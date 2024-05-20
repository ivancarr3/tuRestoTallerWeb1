package com.tallerwebi.dominio.excepcion;

public class NoHayPlatos extends Exception{
    public NoHayPlatos() {
        super("No se encuentran platos disponibles en este momento.");
    }
}
