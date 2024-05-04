package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioRestaurante")
@Transactional
public class ServicioRestauranteImpl implements ServicioRestaurante {

    private RepositorioRestaurante repositorioRestaurante;

    @Autowired
    public ServicioRestauranteImpl(RepositorioRestaurante repositorioRestaurante){
        this.repositorioRestaurante = repositorioRestaurante;
    }

    @Override
    public Restaurante consultar(Long id) {
        return repositorioRestaurante.buscar(id);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorNombre(String nombre) {
        return repositorioRestaurante.buscarPorNombre(nombre);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorEstrellas(Integer estrellas) {
        return repositorioRestaurante.buscarPorEstrellas(estrellas);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorDireccion(String direccion) {
        return repositorioRestaurante.buscarPorDireccion(direccion);
    }

    @Override
    public void crearRestaurante(Restaurante restaurante) throws RestauranteExistente {
        Restaurante restauranteEncontrado = repositorioRestaurante.buscar(restaurante.getId());
        if(restauranteEncontrado != null){
            throw new RestauranteExistente();
        }
        repositorioRestaurante.guardar(restaurante);
    }

    @Override
    public void actualizarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado {
        Restaurante restauranteEncontrado = repositorioRestaurante.buscar(restaurante.getId());
        if(restauranteEncontrado == null){
            throw new RestauranteNoEncontrado();
        }
        repositorioRestaurante.actualizar(restaurante);
    }

    @Override
    public void eliminarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado {
        Restaurante restauranteEncontrado = repositorioRestaurante.buscar(restaurante.getId());
        if(restauranteEncontrado == null){
            throw new RestauranteNoEncontrado();
        }
        repositorioRestaurante.eliminar(restaurante);
    }
}

