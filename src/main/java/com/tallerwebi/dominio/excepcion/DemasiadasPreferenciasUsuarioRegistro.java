package com.tallerwebi.dominio.excepcion;

public class DemasiadasPreferenciasUsuarioRegistro extends Exception {
    public DemasiadasPreferenciasUsuarioRegistro() {super("No se pueden seleccionar más de 3 categorías.");}
}
