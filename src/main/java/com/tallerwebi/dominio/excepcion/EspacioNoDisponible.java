package com.tallerwebi.dominio.excepcion;

public class EspacioNoDisponible  extends Exception{
    public EspacioNoDisponible() {
        super("No hay suficiente espacio disponible en el restaurante.");
    }
}
