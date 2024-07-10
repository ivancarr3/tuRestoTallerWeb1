package com.tallerwebi.dominio.excepcion;

public class NoHayReservasPasadas extends Exception{
    public NoHayReservasPasadas() {
        super("No has asistido a ninguna reserva todavía.");
    }
}
