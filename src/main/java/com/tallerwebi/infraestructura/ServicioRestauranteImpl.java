package com.tallerwebi.infraestructura;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.RepositorioRestaurante;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioRestaurante;

@Service("servicioRestaurante")
@Transactional
public class ServicioRestauranteImpl implements ServicioRestaurante {

    private RepositorioRestaurante repositorioRestaurante;

    @Autowired
    public ServicioRestauranteImpl(RepositorioRestaurante repositorioRestaurante){
        this.repositorioRestaurante = repositorioRestaurante;
    }

    @Override
    public List<Restaurante> get() {
        return repositorioRestaurante.get();
    }

    @Override
    public Restaurante consultar(Long id) {
        return repositorioRestaurante.buscar(id);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorNombre(String nombre) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorNombre(nombre);
        if(restaurantes.size() == 0){
            throw new RestauranteNoEncontrado();
        }
        return repositorioRestaurante.buscarPorNombre(nombre);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorEstrellas(Integer estrellas) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);
        if(restaurantes.size() == 0){
            throw new RestauranteNoEncontrado();
        }
        return repositorioRestaurante.buscarPorEstrellas(estrellas);
    }

    @Override
    public List<Restaurante> consultarRestaurantePorDireccion(String direccion) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorDireccion(direccion);
        if(restaurantes.size() == 0){
            throw new RestauranteNoEncontrado();
        }
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

