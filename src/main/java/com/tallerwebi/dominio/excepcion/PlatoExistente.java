package com.tallerwebi.dominio.excepcion;

public class PlatoExistente extends Exception {
    public PlatoExistente() {
        super("El plato ya existe");
    }
}
