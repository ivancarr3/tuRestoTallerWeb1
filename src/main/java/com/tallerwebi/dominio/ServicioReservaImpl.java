package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service("servicioReserva")
@Transactional
public class ServicioReservaImpl implements ServicioReserva {

    private final RepositorioReserva repositorioReserva;
    private final RepositorioRestaurante repositorioRestaurante;

    @Autowired
    public ServicioReservaImpl(RepositorioReserva repositorioReserva, RepositorioRestaurante repositorioRestaurante){
        this.repositorioReserva = repositorioReserva;
        this.repositorioRestaurante = repositorioRestaurante;
    }

    @Override
    public List<Reserva> buscarReservasDelUsuario(Long idUsuario) {
        return repositorioReserva.buscarReservasDelUsuario(idUsuario);
    }
    
    @Override
    public List<Reserva> buscarTodasLasReservas() throws NoHayReservas {
        List<Reserva> reservas = repositorioReserva.buscarTodasLasReservas();
        if (reservas == null) {
            throw new NoHayReservas();
        }

        return reservas;
    }

    @Override
    public Reserva buscarReserva(Long id) throws ReservaNoEncontrada {
        Reserva reserva =  repositorioReserva.buscarReserva(id);
        if (reserva == null) {
            throw new ReservaNoEncontrada();
        }

        return reserva;
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
        restauranteEncontrado.setEspacioDisponible(restauranteEncontrado.getCapacidadMaxima() - reserva.getCantidadPersonas());
        repositorioRestaurante.actualizar(restauranteEncontrado);
    }

    private boolean validarDatosReserva(String nombreForm, String emailForm, Integer numForm,
                                        Integer dniForm, Integer cantPersonas, Date fechaForm){
        if (nombreForm == null || nombreForm.isEmpty() || emailForm == null || emailForm.isEmpty() ||
                numForm == null || dniForm == null || cantPersonas == null || fechaForm == null) {
            return false;
        }

        Date fechaActual = new Date();

        if (fechaForm.before(fechaActual)) {
            return false;
        }
        if (!emailForm.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
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
