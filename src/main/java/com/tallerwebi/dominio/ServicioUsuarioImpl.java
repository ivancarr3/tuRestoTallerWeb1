package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) throws NoExisteUsuario {
        Usuario usuario = repositorioUsuario.buscarUsuario(email, password);
        if (usuario == null || !usuario.isActivo()) {
            throw new NoExisteUsuario();
        }
        return usuario;
    }

    @Override
    public Usuario consultar(Long id) throws NoExisteUsuario {
        Usuario usuario = repositorioUsuario.consultar(id);
        if (usuario == null) {
            throw new NoExisteUsuario();
        }
        return usuario;
    }

    @Override
    public Usuario buscar(String email) throws NoExisteUsuario {
        Usuario usuario = repositorioUsuario.buscar(email);
        if(usuario == null){
            throw new NoExisteUsuario();
        }
        return usuario;
    }

    @Override
    public void guardar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscar(usuario.getEmail());
        if (usuarioEncontrado != null){
            throw new UsuarioExistente();
        }
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public void modificar(Usuario usuario) throws NoExisteUsuario {
        Usuario usuarioEncontrado = repositorioUsuario.consultar(usuario.getId());
        if(usuarioEncontrado == null){
            throw new NoExisteUsuario();
        }
        repositorioUsuario.modificar(usuario);
    }

    @Override
    public void eliminar(Usuario usuario) throws NoExisteUsuario {
        Usuario usuarioEncontrado = repositorioUsuario.consultar(usuario.getId());
        if(usuarioEncontrado == null){
            throw new NoExisteUsuario();
        }
        repositorioUsuario.eliminar(usuario);
    }

}
