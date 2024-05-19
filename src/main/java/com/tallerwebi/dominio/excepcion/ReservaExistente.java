package com.tallerwebi.dominio.excepcion;

public class ReservaExistente extends Exception {
    public ReservaExistente() {
        super("Ya existe una reserva hecha.");
    }
}
