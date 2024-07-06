package com.tallerwebi.dominio.excepcion;

public class FechaAnterior extends Exception{
    public FechaAnterior() {
        super("La fecha de la reserva no puede ser anterior a la de hoy.");
    }
}
