package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReserva {
    List<Reserva> buscarReservasDelUsuario(Long idUsuario);
    Reserva buscarReserva(Long id);
    void guardar(Reserva reserva);
    void actualizar(Reserva reserva);
    void eliminar(Reserva reserva);
}
