package com.tallerwebi.dominio.excepcion;

public class ReservaNoEncontrada extends Exception {
    public ReservaNoEncontrada() {
        super("Reserva no encontrada.");
    }
}
