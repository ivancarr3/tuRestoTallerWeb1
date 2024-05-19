package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.ReservaExistente;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void guardar(Reserva reserva) throws ReservaExistente {
        List<Reserva> reservasUsuario = repositorioReserva.buscarReservasDelUsuario(reserva.getIdUsuario());
        if (!reservasUsuario.isEmpty()) {
            for (Reserva reservaUsuario : reservasUsuario) {
                if (reservaUsuario.getRestaurante().getId().equals(reserva.getRestaurante().getId()) && esMismoDia(reservaUsuario.getFecha(), reserva.getFecha())) {
                    throw new ReservaExistente();
                }
            }
        }
        repositorioReserva.guardar(reserva);
    }

    private boolean esMismoDia(Date fecha1, Date fecha2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(fecha1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fecha2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
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
    public void crearReserva(Reserva reserva) throws EspacioNoDisponible {
        Restaurante restaurante = reserva.getRestaurante();
        List<Reserva> reservasExistentes = repositorioReserva.getReservasPorRestaurante(restaurante.getId());

        Integer personasTotales = reservasExistentes.stream().mapToInt(Reserva::getCantidadPersonas).sum();
        if (personasTotales + reserva.getCantidadPersonas() > restaurante.getCapacidadMaxima()) {
            throw new EspacioNoDisponible();
        }

        repositorioReserva.guardar(reserva);
    }
}
