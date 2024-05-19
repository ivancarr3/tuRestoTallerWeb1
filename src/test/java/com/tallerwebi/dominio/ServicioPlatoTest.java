package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServicioPlatoTest {

    private ServicioPlato servicioPlato;
    private RepositorioPlato repositorioPlato;
    private List<Plato> platosMock;

    @BeforeEach
    public void init(){
        this.repositorioPlato = mock(RepositorioPlato.class);
        this.servicioPlato = new ServicioPlatoImpl(this.repositorioPlato);
        this.platosMock = new ArrayList<>();
        this.platosMock.add(new Plato(1L, "milanesa", 20000.0, "napolitana", ""));
    }

    @Test
    public void queSePuedanObtenerTodosLosPlatos(){
        // preparacion
        this.platosMock.add(new Plato(2L, "fideos", 20050.0, "con salsa", ""));
        this.platosMock.add(new Plato(3L, "pizza", 20010.0, "jamon y morron", ""));
        when(this.repositorioPlato.get()).thenReturn(this.platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.get();

        // verificacion
        assertThat(platos.size(), equalTo(3));
    }

    @Test
    public void queAlBuscarPlatosPorNombreDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        // preparacion
        this.platosMock.add(new Plato(2L, "milanesa", 20000.0, "de pollo", ""));
        when(this.repositorioPlato.buscarPlatoPorNombre("milanesa")).thenReturn(this.platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.consultarPlatoPorNombre("milanesa");

        // verificacion
        assertThat(platos.size(), equalTo(2));
    }

    @Test
    public void queAlNoEncontrarPlatosPorNombreLanceExcepcion() throws PlatoNoEncontrado {
        // preparacion
        when(this.repositorioPlato.buscarPlatoPorNombre("milanesa")).thenReturn(this.platosMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorNombre("asado");
        });
    }

    @Test
    public void queAlBuscarPlatosPorPrecioDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        // preparacion
        when(this.repositorioPlato.buscarPlatoPorPrecio(20000.0)).thenReturn(this.platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.consultarPlatoPorPrecio(20000.0);

        // verificacion
        assertThat(platos.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarPlatosPorPrecioLanceExcepcion() throws PlatoNoEncontrado {
        // preparacion
        when(this.repositorioPlato.buscarPlatoPorPrecio(20000.0)).thenReturn(this.platosMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorPrecio(15000.0);
        });
    }
}