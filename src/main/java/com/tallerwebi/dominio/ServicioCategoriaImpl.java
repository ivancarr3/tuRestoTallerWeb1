package com.tallerwebi.dominio;

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
    public Categoria getCategoriaDePlato(Long id) {
        return null;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return List.of();
    }
}
