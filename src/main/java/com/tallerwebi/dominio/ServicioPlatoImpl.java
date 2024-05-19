package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.RepositorioPlato;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;

@Service("servicioPlato")
@Transactional
public class ServicioPlatoImpl implements ServicioPlato {

    private final RepositorioPlato repositorioPlato;

    @Autowired
    public ServicioPlatoImpl(RepositorioPlato repositorioPlato){
        this.repositorioPlato = repositorioPlato;
    }

    @Override
    public List<Plato> get() {
        return repositorioPlato.get();
    }

    @Override
    public Plato consultar(Long id) {
        return repositorioPlato.buscar(id);
    }

    @Override
    public List<Plato> consultarPlatoPorNombre(String nombre) throws PlatoNoEncontrado {
        List<Plato> platos = repositorioPlato.buscarPlatoPorNombre(nombre);
        if(platos.isEmpty()){
            throw new PlatoNoEncontrado();
        }
        return repositorioPlato.buscarPlatoPorNombre(nombre);
    }

    @Override
    public List<Plato> consultarPlatoPorPrecio(Double precio) throws PlatoNoEncontrado {
        List<Plato> platos = repositorioPlato.buscarPlatoPorPrecio(precio);
        if(platos.isEmpty()){
            throw new PlatoNoEncontrado();
        }
        return repositorioPlato.buscarPlatoPorPrecio(precio);
    }

    @Override
    public List<Plato> ordenarPorPrecio(String orden) throws PlatoNoEncontrado {
        List<Plato> platos = repositorioPlato.ordenarPorPrecio(orden);
        if(platos.isEmpty()){
            throw new PlatoNoEncontrado();
        }
        return platos;
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

