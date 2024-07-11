package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
@Transactional
public class RepositorioReservaTest {

	@Autowired
	private RepositorioReserva repositorioReserva;

	@Autowired
	private RepositorioRestaurante repositorioRestaurante;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private SessionFactory sessionFactory;

    private final Date fecha = new Date();
    private final Restaurante restauranteInit = new Restaurante(null, "Restaurante Mock", 4.5, "Direccion Mock", "imagenMock.jpg", 50, -34.598940, -58.415550);
    private final Usuario usuarioInit = new Usuario(null, "test@test.com", "123", "USER", "mateo", "fortuna", this.fecha);
    private List<Reserva> reservas = new ArrayList<Reserva>();


	@BeforeEach
	public void init() {
		this.repositorioRestaurante.guardar(this.restauranteInit);
		this.repositorioUsuario.guardar(this.usuarioInit);
		this.reservas.add(crearYGuardarReserva("Mateo", 5));
		this.reservas.add(crearYGuardarReserva("Benjamin", 5));
		this.reservas.add(crearYGuardarReserva("Juan", 5));
	}

	private Reserva crearYGuardarReserva(String nombre, Integer cantidadPersonas) {
		Reserva reserva = new Reserva(null, restauranteInit, nombre, "test@mail.com", 1234, 1234, cantidadPersonas,
				this.fecha, this.usuarioInit, null);
		repositorioReserva.guardar(reserva);
		return reserva;
	}

	@Test
	public void queDevuelvaTodasLasReserva() {
		Reserva reserva = crearYGuardarReserva("Pepe", 4);
		repositorioReserva.guardar(reserva);
		List<Reserva> reservas = repositorioReserva.buscarTodasLasReservas();
		assertNotNull(reservas);
		assertEquals(4, reservas.size());
	}

	@Test
	public void queGuardeReservaCorrectamente() {
		crearYGuardarReserva("Pepe", 4);
		List<Reserva> reservas = repositorioReserva.buscarTodasLasReservas();
		assertThat(reservas.size(), equalTo(4));
	}

	@Test
	public void queDevuelvaReservaPorId() {
		Reserva reserva = crearYGuardarReserva("Pepe", 4);
		Long id = reserva.getId();
		Reserva reservaEncontrada = repositorioReserva.buscarReserva(id);
		assertNotNull(reservaEncontrada);
		assertEquals(reserva.getNombre(), reservaEncontrada.getNombre());
		assertEquals(reserva.getCantidadPersonas(), reservaEncontrada.getCantidadPersonas());
		assertEquals(reserva.getRestaurante().getId(), reservaEncontrada.getRestaurante().getId());
	}

	@Test
	public void queElimineReserva() {
		Reserva reserva = crearYGuardarReserva("Pepe", 4);

		Long id = reserva.getId();
		repositorioReserva.eliminar(reserva);

		assertNull(repositorioReserva.buscarReserva(id));
	}

	@Test
	public void queActualizeReserva() {
		Reserva reserva = crearYGuardarReserva("Pepe", 4);
		Long id = reserva.getId();
		String nombre = "Paola";
		Integer cantidadPersonas = 5;
		String email = "mail@mail.com";
		reserva.setNombre(nombre);
		reserva.setCantidadPersonas(cantidadPersonas);
		reserva.setEmail(email);

		repositorioReserva.actualizar(reserva);
		Reserva reservaActualizadaEncontrada = repositorioReserva.buscarReserva(id);

		assertEquals(reservaActualizadaEncontrada.getNombre(), nombre);
		assertEquals(reservaActualizadaEncontrada.getCantidadPersonas(), cantidadPersonas);
		assertEquals(reservaActualizadaEncontrada.getEmail(), email);
	}

	@Test
	public void queDevuelvaReservasPorRestaurante() {
		crearYGuardarReserva("Pepe", 4);
		List<Reserva> reservasRestaurante = repositorioReserva.getReservasPorRestaurante(this.restauranteInit.getId());
		assertEquals(4, reservasRestaurante.size());
	}

	@Test
	public void queDevuelvaReservasDelUsuario() {
		List<Reserva> reservasUsuario = repositorioReserva.buscarReservasDelUsuario(this.usuarioInit.getId());
		assertNotNull(reservasUsuario);
		assertEquals(3, reservasUsuario.size());
	}

	@Test
	public void queDevuelvaReservasDelUsuarioPasadas() {
		for (Reserva reserva : this.reservas) {
			reserva.setFecha(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
			repositorioReserva.actualizar(reserva);
		}

		List<Reserva> reservasUsuarioPasadas = repositorioReserva.buscarReservasDelUsuarioPasadas(this.usuarioInit.getId());
		assertNotNull(reservasUsuarioPasadas);
		assertEquals(3, reservasUsuarioPasadas.size());
	}

	@Test
	public void queDevuelvaReservasDelRestaurante() {
		List<Reserva> reservasRestaurante = repositorioReserva.buscarReservasDelRestaurante(this.restauranteInit.getId());
		assertNotNull(reservasRestaurante);
		assertEquals(3, reservasRestaurante.size());
	}

	@Test
	public void queDevuelvaEmailsDeUsuariosPorRestaurante() {
		List<String> emails = repositorioReserva.buscarEmailDeUsuariosPorRestaurante(this.restauranteInit.getId());
		assertNotNull(emails);
		assertEquals(3, emails.size());
		assertEquals("test@mail.com", emails.get(0));
	}
}
