package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;

import java.util.Date;
import java.util.List;

public interface ServicioReserva {
    List<Reserva> buscarReservasDelUsuario(Long idUsuario) throws NoHayReservas;
    List<Reserva> buscarReservasDelUsuarioPasadas(Long idUsuario) throws NoHayReservasPasadas;
    Reserva buscarReserva(Long id) throws ReservaNoEncontrada;
    void actualizar(Reserva reserva) throws ReservaNoEncontrada;
    void cancelarReserva(Reserva reserva) throws ReservaNoEncontrada;
    Reserva crearReserva(Restaurante restauranteEncontrado, String nombre_form, String email_form, Integer num_form,
                      Integer dni_form, Integer cant_personas, Date fecha_form, Usuario usuario) throws EspacioNoDisponible, NoExisteUsuario, ReservaNoEncontrada;
	List<Reserva> buscarTodasLasReservas() throws NoHayReservas;
	List<Reserva> buscarReservasDelRestaurante(Long idRestaurante) throws NoHayReservas;
    List<String> obtenerEmailsUsuariosPorRestaurante(Long idRestaurante) throws NoHayReservas;
}
