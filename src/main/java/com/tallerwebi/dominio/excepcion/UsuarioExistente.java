package com.tallerwebi.dominio.excepcion;

public class UsuarioExistente extends Exception {
    public UsuarioExistente() {
        super("Ese usario ya existe. Deberá elegir otro.");
    }
}
