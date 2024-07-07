package com.tallerwebi.servicio;

import com.tallerwebi.dominio.AdministradorDeRestaurante;

public interface ServicioLoginAdministradorDeRestaurante {

    AdministradorDeRestaurante buscarAdministradorDeRestaurante(String email, String pass);

}
