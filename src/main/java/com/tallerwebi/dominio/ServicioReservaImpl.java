package com.tallerwebi.dominio;

import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.ReservaExistente;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("servicioReserva")
@Transactional
public class ServicioReservaImpl implements ServicioReserva {

    private final RepositorioReserva repositorioReserva;

    @Autowired
    public ServicioReservaImpl(RepositorioReserva repositorioReserva){
        this.repositorioReserva = repositorioReserva;
    }

    @Override
    public List<Reserva> buscarReservasDelUsuario(Long idUsuario) {
        return repositorioReserva.buscarReservasDelUsuario(idUsuario);
    }

    @Override
    public Reserva buscarReserva(Long id) {
        return repositorioReserva.buscarReserva(id);
    }



    @Override
    public void actualizar(Reserva reserva) throws ReservaNoEncontrada {
        Reserva reservaNoEncontrada = repositorioReserva.buscarReserva(reserva.getId());
        if(reservaNoEncontrada == null){
            throw new ReservaNoEncontrada();
        }
        repositorioReserva.actualizar(reserva);
    }

    @Override
    public void cancelarReserva(Reserva reserva) throws ReservaNoEncontrada {
        Reserva reservaNoEncontrada = repositorioReserva.buscarReserva(reserva.getId());
        if(reservaNoEncontrada == null){
            throw new ReservaNoEncontrada();
        }
        repositorioReserva.eliminar(reserva);
    }

    @Override
    public void crearReserva(Restaurante restauranteEncontrado, String nombre_form, String email_form, Integer num_form,
                             Integer dni_form, Integer cant_personas, Date fecha_form) throws EspacioNoDisponible, DatosInvalidosReserva {
        if(!validarDatosReserva(nombre_form, email_form, num_form,
                dni_form, cant_personas, fecha_form)){
            System.out.println("Datos inválidos para la reserva");
            throw new DatosInvalidosReserva();
        }
        Reserva reserva = new Reserva(null, restauranteEncontrado, nombre_form, email_form, num_form,
                dni_form, cant_personas, fecha_form);

        if (!verificarEspacioDisponible(reserva)) {
            throw new EspacioNoDisponible();
        }
        repositorioReserva.guardar(reserva);
    }

    private boolean validarDatosReserva(String nombre_form, String email_form, Integer num_form,
                                        Integer dni_form, Integer cant_personas, Date fecha_form){
        if (nombre_form == null || nombre_form.isEmpty() || email_form == null || email_form.isEmpty() ||
                num_form == null || dni_form == null || cant_personas == null || fecha_form == null) {
            return false;
        }

        Date fechaActual = new Date();

        if (fecha_form.before(fechaActual)) {
            return false;
        }
        if (!email_form.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }
        return true;
    }

    private boolean verificarEspacioDisponible(Reserva reserva) {
        Restaurante restaurante = reserva.getRestaurante();
        List<Reserva> reservasExistentes = repositorioReserva.getReservasPorRestaurante(restaurante.getId());

        int personasTotales = reservasExistentes.stream().mapToInt(Reserva::getCantidadPersonas).sum();
        return personasTotales + reserva.getCantidadPersonas() <= restaurante.getCapacidadMaxima();
    }
}
