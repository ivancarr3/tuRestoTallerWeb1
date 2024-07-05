package com.tallerwebi.dominio.excepcion;

public class NoExisteUsuario extends Exception {
    public NoExisteUsuario() {
        super("Credenciales inválidas.");
    }
}
