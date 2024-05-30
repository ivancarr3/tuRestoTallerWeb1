package com.tallerwebi.servicio;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.ReservaExistente;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;

import java.util.Date;
import java.util.List;

public interface ServicioReserva {
    List<Reserva> buscarReservasDelUsuario(Long idUsuario);
    Reserva buscarReserva(Long id);
    //void guardar(Reserva reserva) throws ReservaExistente;
    void actualizar(Reserva reserva) throws ReservaNoEncontrada;
    void cancelarReserva(Reserva reserva) throws ReservaNoEncontrada;
    //void crearReserva(Reserva reserva) throws EspacioNoDisponible;
    void crearReserva(Restaurante restauranteEncontrado, String nombre_form, String email_form, Integer num_form,
                      Integer dni_form, Integer cant_personas, Date fecha_form) throws EspacioNoDisponible, DatosInvalidosReserva;
	List<Reserva> buscarTodasLasReservas();
}
