package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioUsuario {

    Usuario buscarUsuario(String email, String password) throws NoExisteUsuario;
    void guardar(Usuario usuario) throws UsuarioExistente;
    Usuario buscar(String email) throws NoExisteUsuario;
    Usuario consultar(Long id) throws NoExisteUsuario;
    void modificar(Usuario usuario) throws NoExisteUsuario;
    void eliminar(Usuario usuario) throws NoExisteUsuario;
}