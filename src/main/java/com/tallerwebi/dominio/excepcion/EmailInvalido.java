package com.tallerwebi.dominio.excepcion;

public class EmailInvalido extends Exception{
    public EmailInvalido() {
        super("El email ingresado no es válido.");
    }
}
