package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.servicio.ServicioLoginAdministradorDeRestaurante;

@Service
public class ServicioLoginAdministradorDeRestauranteImpl implements ServicioLoginAdministradorDeRestaurante {

    private final RepositorioAdministradorRestaurante repositorioAdministradorRestaurante;

    @Autowired
    public ServicioLoginAdministradorDeRestauranteImpl(RepositorioAdministradorRestaurante repositorioAdministradorRestaurante) {
        this.repositorioAdministradorRestaurante = repositorioAdministradorRestaurante;
    }

    @Override
    public AdministradorDeRestaurante buscarAdministradorDeRestaurante(String email, String pass) {
        return repositorioAdministradorRestaurante.buscarAdministradorDeRestaurante(email, pass);
    }

}
