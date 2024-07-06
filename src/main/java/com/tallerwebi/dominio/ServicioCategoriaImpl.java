package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoHayCategorias;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.servicio.ServicioCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioCategoria")
@Transactional
public class ServicioCategoriaImpl implements ServicioCategoria {

    private final RepositorioCategoria repositorioCategoria;

    @Autowired
    public ServicioCategoriaImpl(RepositorioCategoria repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    @Override
    public List<Categoria> get() throws NoHayCategorias {
        List<Categoria> categorias = repositorioCategoria.get();
        if (categorias == null) {
            throw new NoHayCategorias();
        }

        return categorias;
    }

    @Override
    public Categoria buscarCategoria(Long id) throws NoHayCategorias {
        Categoria categoria = repositorioCategoria.buscarCategoria(id);
        if (categoria == null) {
            throw new NoHayCategorias();
        }

        return categoria;
    }

    @Override
    public void crearCategoria(Categoria categoria) {
        repositorioCategoria.guardar(categoria);
    }

    @Override
    public void editarCategoria(Categoria categoria) {
        repositorioCategoria.modificar(categoria);
    }

    @Override
    public void eliminarCategoria(Categoria categoria) {
        repositorioCategoria.eliminar(categoria);
    }
}
