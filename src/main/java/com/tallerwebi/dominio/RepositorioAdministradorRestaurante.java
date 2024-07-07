package com.tallerwebi.dominio;

public interface RepositorioAdministradorRestaurante {

    AdministradorDeRestaurante buscarAdministradorDeRestaurante(String email, String pass);
    AdministradorDeRestaurante obtenerAdministradorDeRestaurantePorId(Long id);

}
