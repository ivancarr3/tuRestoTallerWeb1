package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioNoActivado;

public interface ServicioLogin {
    Usuario consultarUsuario(String email, String password) throws UsuarioNoActivado;
    void registrar(Usuario usuario) throws UsuarioExistente;
    void activarUsuario(String token) throws NoExisteUsuario;
}
