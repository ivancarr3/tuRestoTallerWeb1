package com.tallerwebi.dominio;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioRestaurante")
@Transactional
public class ServicioRestauranteImpl implements ServicioRestaurante {

    private final RepositorioRestaurante repositorioRestaurante;
    private final ServicioReserva servicioReserva;
    private final ServicioGeocoding servicioGeocoding;

    @Autowired
    public ServicioRestauranteImpl(RepositorioRestaurante repositorioRestaurante, ServicioReserva servicioReserva,
            ServicioGeocoding servicioGeocoding) {
        this.repositorioRestaurante = repositorioRestaurante;
        this.servicioReserva = servicioReserva;
        this.servicioGeocoding = servicioGeocoding;
    }

    @Override
    public List<Restaurante> get() throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.get();
        if (restaurantes == null || restaurantes.isEmpty()) {
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
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorNombre(nombre);
        if (restaurantes.isEmpty()) {
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorFiltros(Double estrellas, String tipoDeOrden)
            throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes;
        if (estrellas != null && tipoDeOrden != null) {
            restaurantes = repositorioRestaurante.buscarPorEstrellasYOrdenar(estrellas, tipoDeOrden);
        } else if (estrellas != null) {
            restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);
        } else if (tipoDeOrden != null) {
            restaurantes = repositorioRestaurante.ordenarPorEstrellas(tipoDeOrden);
        } else {
            restaurantes = repositorioRestaurante.get();
        }

        if (restaurantes.isEmpty()) {
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorEstrellas(Double estrellas) throws RestauranteNoEncontrado {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEstrellas(estrellas);
        if (restaurantes.isEmpty()) {
            throw new RestauranteNoEncontrado();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorEspacio(Integer capacidad) throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorEspacio(capacidad);
        if (restaurantes.isEmpty()) {
            throw new NoHayRestaurantes();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarRestaurantePorDireccion(String direccion) throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.buscarPorDireccion(direccion);
        if (restaurantes.isEmpty()) {
            throw new NoHayRestaurantes();
        }
        return restaurantes;
    }

    @Override
    public List<Restaurante> consultarOrdenPorEstrellas(String tipoDeOrden) throws NoHayRestaurantes {
        List<Restaurante> restaurantes = repositorioRestaurante.ordenarPorEstrellas(tipoDeOrden);
        if (restaurantes.isEmpty()) {
            throw new NoHayRestaurantes();
        }
        return restaurantes;
    }

    @Override
    public void crearRestaurante(Restaurante restaurante) throws RestauranteExistente {
        if (repositorioRestaurante.buscar(restaurante.getId()) != null) {
            throw new RestauranteExistente();
        }
        repositorioRestaurante.guardar(restaurante);
    }

    @Override
    public void actualizarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado {
        if (repositorioRestaurante.buscar(restaurante.getId()) == null) {
            throw new RestauranteNoEncontrado();
        }
        repositorioRestaurante.actualizar(restaurante);
    }

    @Override
    public void eliminarRestaurante(Restaurante restaurante) throws RestauranteNoEncontrado {
        if (repositorioRestaurante.buscar(restaurante.getId()) == null) {
            throw new RestauranteNoEncontrado();
        }
        repositorioRestaurante.eliminar(restaurante);
    }

    @Override
    public List<Restaurante> filtrarPorDireccion(String direccion, double radio)
            throws NoHayRestaurantes, NoExisteDireccion {
        ServicioGeocoding.Coordenadas coordenadas = servicioGeocoding.obtenerCoordenadas(direccion);
        if (coordenadas == null) {
            throw new NoExisteDireccion();
        }

        double latitud = coordenadas.getLatitud();
        double longitud = coordenadas.getLongitud();

        List<Restaurante> restaurantes = repositorioRestaurante.get();
        List<Restaurante> restaurantesEncontrados = restaurantes.stream()
                .filter(r -> distanciaEntreCoordenadas(latitud, longitud, r.getLatitud(), r.getLongitud()) <= radio)
                .collect(Collectors.toList());
        if (restaurantesEncontrados.isEmpty()) {
            throw new NoHayRestaurantes();
        }
        return restaurantesEncontrados;
    }

    private double distanciaEntreCoordenadas(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA * c;
    }

    @Override
    public List<Restaurante> obtenerRestaurantesDeshabilitados() {
        return repositorioRestaurante.obtenerRestaurantesDeshabilitados();
    }

    @Override
    public List<Restaurante> obtenerRestaurantesHabilitados() {
        return repositorioRestaurante.obtenerRestaurantesHabilitados();
    }

    @Override
    public void habilitarRestaurante(Long id) {
        repositorioRestaurante.habilitarRestaurante(id);
    }

    @Override
    public void deshabilitarRestaurante(Long id) {
        repositorioRestaurante.deshabilitarRestaurante(id);
    }

    @Override
    public void eliminarRestaurantePorId(Long id) {
        repositorioRestaurante.eliminarRestaurantePorId(id);
    }

}
