package com.tallerwebi.dominio.excepcion;

public class UsuarioNoActivado extends Exception{
    public UsuarioNoActivado() {
        super("Esta cuenta no está activada.");
    }
}
