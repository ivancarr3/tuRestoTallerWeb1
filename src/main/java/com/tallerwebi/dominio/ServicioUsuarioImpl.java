package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioCategoria repositorioCategoria;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario, RepositorioCategoria repositorioCategoria){
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioCategoria = repositorioCategoria;
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
    public Usuario buscarUsuarioPorEmail(String email) throws NoExisteUsuario{
        Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email);
        if (usuario == null || !usuario.isActivo()) {
            throw new NoExisteUsuario();
        }
        return usuario;
    }

    @Override
    public void agregarCategoriaPrefUser(String email, Long categoriaId) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email);
        Categoria categoria = repositorioCategoria.buscarCategoria(categoriaId);
        if(usuario != null && categoria != null){
            usuario.getCategorias().add(categoria);
            repositorioUsuario.guardar(usuario);
            //repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public List<Categoria> obtenerCategoriasDisponibles(List<Categoria> categoriasPref) {
        List<Categoria> todasCategorias = repositorioCategoria.get();
        todasCategorias.removeAll(categoriasPref);
        return todasCategorias;
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
