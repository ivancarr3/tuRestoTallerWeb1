package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.servicio.ServicioAdministradorRestaurante;

@Service
public class ServicioAdministradorRestauranteImpl implements ServicioAdministradorRestaurante {

    RepositorioAdministradorRestaurante repositorioAdministradorRestaurante;

    @Autowired
    public ServicioAdministradorRestauranteImpl(
            RepositorioAdministradorRestaurante repositorioAdministradorRestaurante) {
        this.repositorioAdministradorRestaurante = repositorioAdministradorRestaurante;
    }

    @Override
    public AdministradorDeRestaurante obtenerAdministradorPorId(Long id) {
        return repositorioAdministradorRestaurante.obtenerAdministradorDeRestaurantePorId(id);
    }

}
