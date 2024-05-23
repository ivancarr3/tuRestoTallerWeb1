package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.RestauranteExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioRestaurante;

@Service("servicioRestaurante")
@Transactional
public class ServicioRestauranteImpl implements ServicioRestaurante {

    private final RepositorioRestaurante repositorioRestaurante;
    private final ServicioReserva servicioReserva;

    @Autowired
    public ServicioRestauranteImpl(RepositorioRestaurante repositorioRestaurante, ServicioReserva servicioReserva){
        this.repositorioRestaurante = repositorioRestaurante;
        this.servicioReserva = servicioReserva;
    }

    @Override
    public List<Restaurante> get() throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.get();
        if (restaurantes == null) {
            throw new NoHayRestaurantes();
        }

        return restaurantes;
    }

    @Override
    public Restaurante consultar(Long id) throws RestauranteNoEncontrado {
        Restaurante restaurante = repositorioRestaurante.buscar(id);
        if (restaurante == null) {
            throw new RestauranteNoEncontrado();
        }
        return restaurante;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorNombre(String nombre) throws RestauranteNoEncontrado {
    	if(nombre == null) {
    		return repositorioRestaurante.get();
    	}
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorNombre(nombre);
        if(restaurantes.isEmpty()){
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }
    

	@Override
	public List<Restaurante> consultarRestaurantePorFiltros(Double estrellas, String tipoDeOrden) throws RestauranteNoEncontrado {
		
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		
		if (estrellas != null && tipoDeOrden != null) {
			restaurantes = repositorioRestaurante.buscarPorEstrellasYOrdenar(estrellas, tipoDeOrden);
		} else if (estrellas != null) {
			restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);
		} else if (tipoDeOrden != null) {
			restaurantes = repositorioRestaurante.ordenarPorEstrellas(tipoDeOrden);
		}
		
        if (restaurantes.isEmpty()) {
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
	}

    @Override
    public List<Restaurante> consultarRestaurantePorEstrellas(Double estrellas) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);
        if(restaurantes.isEmpty()){
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorDireccion(String direccion) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorDireccion(direccion);
        if(restaurantes.isEmpty()){
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarOrdenPorEstrellas(String tipoDeOrden) throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.ordenarPorEstrellas(tipoDeOrden);
        if(restaurantes.isEmpty()){
            throw new NoHayRestaurantes();
        }
        return restaurantes;
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

    /*public void realizarReserva(Reserva reserva) throws Exception {
        try {
            servicioReserva.crearReserva(reserva);
        } catch (Exception e) {
            throw new Exception("Error al realizar la reserva.");
        }
    }*/

}

