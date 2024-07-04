package com.tallerwebi.dominio;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    Usuario consultar(Long id);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    void eliminar(Usuario usuario);
    Usuario buscarPorToken(String token);
}

