package com.tallerwebi.dominio.excepcion;

public class NoHayRestaurantes extends Exception{
    public NoHayRestaurantes() {
        super("No se encuentran restaurantes disponibles en este momento.");
    }
}
