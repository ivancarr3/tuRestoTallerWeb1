package com.tallerwebi.dominio;

import com.tallerwebi.dominio.config.HibernateTestConfig;
import com.tallerwebi.dominio.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
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
        this.reservas.add(crearYGuardarReserva("Mateo",5));
        this.reservas.add(crearYGuardarReserva("Benjamin",5));
        this.reservas.add(crearYGuardarReserva("Juan",5));
    }

    private Reserva crearYGuardarReserva(String nombre, Integer cantidadPersonas) {
        Reserva reserva = new Reserva(null, restauranteInit, nombre, "test@mail.com", 1234, 1234, cantidadPersonas, this.fecha, this.usuarioInit);
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
}
