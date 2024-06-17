package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.*;

import java.util.Date;
import java.util.List;

public interface ServicioReserva {
    List<Reserva> buscarReservasDelUsuario(Long idUsuario);
    Reserva buscarReserva(Long id) throws ReservaNoEncontrada;
    //void guardar(Reserva reserva) throws ReservaExistente;
    void actualizar(Reserva reserva) throws ReservaNoEncontrada;
    void cancelarReserva(Reserva reserva) throws ReservaNoEncontrada;
    //void crearReserva(Reserva reserva) throws EspacioNoDisponible;
    Reserva crearReserva(Restaurante restauranteEncontrado, String nombre_form, String email_form, Integer num_form,
                      Integer dni_form, Integer cant_personas, Date fecha_form) throws EspacioNoDisponible;
	List<Reserva> buscarTodasLasReservas() throws NoHayReservas;
}
