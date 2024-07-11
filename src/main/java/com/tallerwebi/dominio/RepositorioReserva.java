package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReserva {
    List<Reserva> buscarReservasDelUsuario(Long idUsuario);
    List<Reserva> buscarReservasDelUsuarioPasadas(Long idUsuario);
    Reserva buscarReserva(Long id);
    List<Reserva> getReservasPorRestaurante(Long idRestaurante);
    void guardar(Reserva reserva);
    void actualizar(Reserva reserva);
    void eliminar(Reserva reserva);
	List<Reserva> buscarTodasLasReservas();
	List<Reserva> buscarReservasDelRestaurante(Long idRestaurante);
    List<String> buscarEmailDeUsuariosPorRestaurante(Long idRestaurante);
    Reserva findByPaymentId(String paymentId);
}
