package com.tallerwebi.servicio;

import com.tallerwebi.dominio.AdministradorDeRestaurante;

public interface ServicioAdministradorRestaurante {
    AdministradorDeRestaurante obtenerAdministradorPorId(Long id);
}
