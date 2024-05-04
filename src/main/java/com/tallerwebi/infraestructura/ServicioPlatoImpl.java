package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioPlato")
@Transactional
public class ServicioPlatoImpl implements ServicioPlato {

    private RepositorioPlato repositorioPlato;

    @Autowired
    public ServicioPlatoImpl(RepositorioPlato repositorioPlato){
        this.repositorioPlato = repositorioPlato;
    }

    @Override
    public Plato consultar(Long id) {
        return repositorioPlato.buscar(id);
    }

    @Override
    public List<Plato> consultarPlatoPorNombre(String nombre) {
        return repositorioPlato.buscarPlatoPorNombre(nombre);
    }

    @Override
    public List<Plato> consultarPlatoPorPrecio(Integer precio) {
        return repositorioPlato.buscarPlatoPorPrecio(precio);
    }

    @Override
    public void crearPlato(Plato plato) throws PlatoExistente {
        Plato platoEncontrado = repositorioPlato.buscar(plato.getId());
        if(platoEncontrado != null){
            throw new PlatoExistente();
        }
        repositorioPlato.guardarPlato(plato);
    }

    @Override
    public void actualizarPlato(Plato plato) throws PlatoNoEncontrado {
        Plato platoEncontrado = repositorioPlato.buscar(plato.getId());
        if(platoEncontrado == null){
            throw new PlatoNoEncontrado();
        }
        repositorioPlato.modificarPlato(plato);
    }

    @Override
    public void eliminarPlato(Plato plato) throws PlatoNoEncontrado {
        Plato platoEncontrado = repositorioPlato.buscar(plato.getId());
        if(platoEncontrado == null){
            throw new PlatoNoEncontrado();
        }
        repositorioPlato.eliminarPlato(plato);
    }
}

