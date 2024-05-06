package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.infraestructura.ServicioPlatoImpl;
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

    @BeforeEach
    public void init(){
        this.repositorioPlato = mock(RepositorioPlato.class);
        this.servicioPlato = new ServicioPlatoImpl(this.repositorioPlato);
    }

    @Test
    public void queSePuedanObtenerTodosLosPlatos(){
        // preparacion
        List<Plato> platosMock = new ArrayList<>();
        platosMock.add(new Plato(1L, "nombre1", 200, "desc"));
        platosMock.add(new Plato(2L, "nombre2", 300, "desc2"));
        platosMock.add(new Plato(3L, "nombre3", 400, "desc3"));
        when(this.repositorioPlato.get()).thenReturn(platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.get();

        // verificacion
        assertThat(platos.size(), equalTo(3)); // Existan 3 elementos
    }

    @Test
    public void queAlBuscarPlatosPorNombreDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        // preparacion
        List<Plato> platosMock = new ArrayList<>();
        platosMock.add(new Plato(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioPlato.buscarPlatoPorNombre("nombre1")).thenReturn(platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.consultarPlatoPorNombre("nombre1");

        // verificacion
        assertThat(platos.size(), equalTo(1)); // Existan 1 elementos
    }

    @Test
    public void queAlNoEncontrarPlatosPorNombreLanceExcepcion() throws PlatoNoEncontrado {
        // preparacion
        List<Plato> platosMock = new ArrayList<>();
        platosMock.add(new Plato(1L, "nombre1", 2, "direccion1"));
        when(this.repositorioPlato.buscarPlatoPorNombre("nombre1")).thenReturn(platosMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorNombre("nombre2");
        });
    }

    @Test
    public void queAlBuscarPlatosPorPrecioDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        // preparacion
        List<Plato> platosMock = new ArrayList<>();
        platosMock.add(new Plato(1L, "nombre1", 200, "direccion1"));
        when(this.repositorioPlato.buscarPlatoPorPrecio(200)).thenReturn(platosMock);

        // ejecucion
        List<Plato> platos = this.servicioPlato.consultarPlatoPorPrecio(200);

        // verificacion
        assertThat(platos.size(), equalTo(1)); // Existan 1 elementos
    }

    @Test
    public void queAlNoEncontrarPlatosPorPrecioLanceExcepcion() throws PlatoNoEncontrado {
        // preparacion
        List<Plato> platosMock = new ArrayList<>();
        platosMock.add(new Plato(1L, "nombre1", 200, "direccion1"));
        when(this.repositorioPlato.buscarPlatoPorPrecio(200)).thenReturn(platosMock);

        // ejecucion y verificacion de la excepcion
        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorPrecio(566);
        });
    }
}