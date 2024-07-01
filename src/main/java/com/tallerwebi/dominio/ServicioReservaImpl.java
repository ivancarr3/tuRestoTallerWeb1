package com.tallerwebi.dominio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import com.tallerwebi.servicio.ServicioReserva;

@Service("servicioReserva")
@Transactional
public class ServicioReservaImpl implements ServicioReserva {

	private final RepositorioReserva repositorioReserva;
	private final RepositorioRestaurante repositorioRestaurante;
	private final RepositorioUsuario repositorioUsuario;
	private final Email emailSender;

	@Autowired
	public ServicioReservaImpl(RepositorioReserva repositorioReserva, RepositorioRestaurante repositorioRestaurante,
			Email emailSender, RepositorioUsuario repositorioUsuario) {
		this.repositorioReserva = repositorioReserva;
		this.repositorioRestaurante = repositorioRestaurante;
		this.emailSender = emailSender;
		this.repositorioUsuario = repositorioUsuario;
	}

	@Override
	public List<Reserva> buscarReservasDelUsuario(Long idUsuario) throws NoHayReservas {
		List<Reserva> reservas;
		try {
			reservas = repositorioReserva.buscarReservasDelUsuario(idUsuario);
			if (reservas.isEmpty()) {
				throw new NoHayReservas();
			}
		} catch (NoHayReservas e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error al buscar reservas del usuario", e);
		}
		return reservas;
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
		Reserva reserva = repositorioReserva.buscarReserva(id);
		if (reserva == null) {
			throw new ReservaNoEncontrada();
		}

		return reserva;
	}

	@Override
	public void actualizar(Reserva reserva) throws ReservaNoEncontrada {
		Reserva reservaNoEncontrada = repositorioReserva.buscarReserva(reserva.getId());
		if (reservaNoEncontrada == null) {
			throw new ReservaNoEncontrada();
		}
		repositorioReserva.actualizar(reserva);
	}

	@Override
	public void cancelarReserva(Reserva reserva) throws ReservaNoEncontrada {
		Reserva reservaNoEncontrada = repositorioReserva.buscarReserva(reserva.getId());
		if (reservaNoEncontrada == null) {
			throw new ReservaNoEncontrada();
		}
		repositorioReserva.eliminar(reserva);
	}

	@Override
	public Reserva crearReserva(Restaurante restauranteEncontrado, String nombre_form, String email_form,
								Integer num_form, Integer dni_form, Integer cant_personas, Date fecha_form, Usuario usuario) throws EspacioNoDisponible, NoExisteUsuario {
		if (usuario == null) {
			throw new NoExisteUsuario();
		}

		Reserva reserva = new Reserva(null, restauranteEncontrado, nombre_form, email_form, num_form, dni_form,
				cant_personas, fecha_form, usuario);

		if (!verificarEspacioDisponible(reserva)) {
			throw new EspacioNoDisponible();
		}

		repositorioReserva.guardar(reserva);
		restauranteEncontrado.setEspacioDisponible(restauranteEncontrado.getCapacidadMaxima() - reserva.getCantidadPersonas());
		repositorioRestaurante.actualizar(restauranteEncontrado);
		this.sendMail(nombre_form, restauranteEncontrado.getNombre(), cant_personas, fecha_form, email_form);
		return reserva;
	}

	private boolean verificarEspacioDisponible(Reserva reserva) {
		Restaurante restaurante = reserva.getRestaurante();
		List<Reserva> reservasExistentes = repositorioReserva.getReservasPorRestaurante(restaurante.getId());

		Integer personasTotales = reservasExistentes.stream().mapToInt(Reserva::getCantidadPersonas).sum();
		return personasTotales + reserva.getCantidadPersonas() <= restaurante.getCapacidadMaxima();
	}

	private void sendMail(String nombre_form, String nombreRestaurante, Integer cant_personas, Date fecha_form,
			String email_form) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy 'a las' HH:mm",
				new Locale("es", "ES"));
		String fechaFormateada = dateFormat.format(fecha_form);
		String subject = "Confirmación de Reserva";
		String text = "Hola " + nombre_form + ",\n\n" + "Tu reserva ha sido confirmada en " + nombreRestaurante
				+ " para " + cant_personas + " personas el " + fechaFormateada + ".\n\n" + "Gracias por tu reserva.\n"
				+ "Saludos,\n" + nombreRestaurante;

		this.emailSender.sendSimpleMessage(email_form, subject, text);
	}
}
