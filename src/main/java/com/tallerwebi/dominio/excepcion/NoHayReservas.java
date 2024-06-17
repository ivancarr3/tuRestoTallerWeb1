package com.tallerwebi.dominio.excepcion;

public class NoHayReservas extends Exception{
    public NoHayReservas() {
        super("No se encontraron reservas.");
    }
}
