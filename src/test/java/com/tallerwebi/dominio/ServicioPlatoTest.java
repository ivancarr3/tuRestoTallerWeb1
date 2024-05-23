package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        this.platosMock.add(new Plato(1L, "milanesa de carne", 20000.0, "napolitana", ""));
        this.platosMock.add(new Plato(2L, "pollo", 10000.0, "a la mostaza", ""));
        this.platosMock.add(new Plato(3L, "asado", 17000.0, "con hueso", ""));
        this.platosMock.add(new Plato(4L, "milanesa de pollo", 17000.0, "con queso", ""));
    }

    @Test
    public void queSePuedanObtenerTodosLosPlatos() throws NoHayPlatos{
        when(this.repositorioPlato.get()).thenReturn(this.platosMock);

        List<Plato> platos = this.servicioPlato.get();

        assertThat(platos.size(), equalTo(4));
    }

    @Test
    public void queLanceExcepcionSiNoExistenPlatos() throws NoHayPlatos {
        when(this.repositorioPlato.get()).thenReturn(null);

        assertThrows(NoHayPlatos.class, () -> this.servicioPlato.get());
    }

    @Test
    public void queSePuedanObtenerUnPlatoPorId() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscar(1L)).thenReturn(this.platosMock.get(0));

        Plato plato = this.servicioPlato.consultar(1L);

        assertEquals(plato.getNombre(), this.platosMock.get(0).getNombre());
    }

    @Test
    public void queAlNoEncontrarPlatoPorIdLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscar(1L)).thenReturn(null);

        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultar(1L);
        });
    }

    @Test
    public void queAlBuscarPlatosPorNombreDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        List<Plato> platosPorNombre = List.of(
                this.platosMock.get(0),  // Primer elemento
                this.platosMock.get(this.platosMock.size() - 1)  // Último elemento
        );
        when(this.repositorioPlato.buscarPlatoPorNombre("milanesa")).thenReturn(platosPorNombre);

        List<Plato> platos = this.servicioPlato.consultarPlatoPorNombre("milanesa");

        assertThat(platos.size(), equalTo(2));
    }

    @Test
    public void queAlNoEncontrarPlatosPorNombreLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscarPlatoPorNombre("milanesa")).thenReturn(this.platosMock);

        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorNombre("asado");
        });
    }

    @Test
    public void queAlBuscarPlatosPorPrecioDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        List<Plato> platoPorPrecio = List.of(this.platosMock.get(0));
        when(this.repositorioPlato.buscarPlatoPorPrecio(20000.0)).thenReturn(platoPorPrecio);

        List<Plato> platos = this.servicioPlato.consultarPlatoPorPrecio(20000.0);

        assertThat(platos.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarPlatosPorPrecioLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscarPlatoPorPrecio(20000.0)).thenReturn(this.platosMock);

        assertThrows(PlatoNoEncontrado.class, () -> {
            this.servicioPlato.consultarPlatoPorPrecio(15000.0);
        });
    }

    @Test
    public void queOrdenePlatosPorPrecioAscendente() throws NoHayPlatos{
        Collections.sort(this.platosMock, Comparator.comparingDouble(Plato::getPrecio));
        when(this.repositorioPlato.ordenarPorPrecio("ASC")).thenReturn(this.platosMock);

        List<Plato> platos = this.servicioPlato.ordenarPorPrecio("ASC");

        assertThat(platos.size(), equalTo(4));
        for (int i = 0; i < platos.size() - 1; i++) {
            assertThat(platos.get(i).getPrecio(), lessThanOrEqualTo(platos.get(i + 1).getPrecio()));
        }
    }

    @Test
    public void queOrdenePlatosPorPrecioDescendente() throws NoHayPlatos{
        Collections.sort(this.platosMock, Comparator.comparingDouble(Plato::getPrecio).reversed());
        when(this.repositorioPlato.ordenarPorPrecio("DESC")).thenReturn(this.platosMock);

        List<Plato> platos = this.servicioPlato.ordenarPorPrecio("DESC");

        assertThat(platos.size(), equalTo(4));
        for (int i = 0; i < platos.size() - 1; i++) {
            assertThat(platos.get(i).getPrecio(), greaterThanOrEqualTo(platos.get(i + 1).getPrecio()));
        }
    }

    @Test
    public void queSeCreePlatoCorrectamente() throws PlatoExistente {
        when(repositorioPlato.buscar(anyLong())).thenReturn(null);
        Plato nuevoPlato = new Plato(5L, "sopa", 9000.0, "con lentejas", "");

        servicioPlato.crearPlato(nuevoPlato);

        verify(repositorioPlato, times(1)).guardarPlato(nuevoPlato);
    }

    @Test
    public void queLanceExcepcionSiSeCreaUnPlatoConElMismoId() throws PlatoExistente {
        Plato platoExistente = new Plato(5L, "sopa", 9000.0, "con lentejas", "");
        when(repositorioPlato.buscar(5L)).thenReturn(platoExistente);

        Plato nuevoPlato = new Plato(5L, "sopa", 9000.0, "con lentejas", "");

        assertThrows(PlatoExistente.class, () -> servicioPlato.crearPlato(nuevoPlato));

        verify(repositorioPlato, never()).guardarPlato(nuevoPlato);
    }

    @Test
    public void queSeActualizePlatoCorrectamente() throws PlatoNoEncontrado {
        Plato platoEncontrado = this.platosMock.get(0);
        platoEncontrado.setPrecio(12500.0);
        when(repositorioPlato.buscar(anyLong())).thenReturn(platoEncontrado);

        servicioPlato.actualizarPlato(platoEncontrado);

        verify(repositorioPlato, times(1)).modificarPlato(platoEncontrado);
    }

    @Test
    public void queLanceExcepcionSiQuiereActualizarUnPlatoQueNoExiste() throws PlatoNoEncontrado {
        Plato plato = new Plato(56L, "sopa", 9000.0, "con lentejas", "");

        assertThrows(PlatoNoEncontrado.class, () -> servicioPlato.actualizarPlato(plato));
        verify(repositorioPlato, never()).modificarPlato(plato);
    }

    @Test
    public void queSeElimineUnPlatoCorrectamente() throws PlatoNoEncontrado {
        Plato plato = this.platosMock.get(0);
        when(repositorioPlato.buscar(anyLong())).thenReturn(plato);

        servicioPlato.eliminarPlato(plato);

        verify(repositorioPlato, times(1)).eliminarPlato(plato);
    }

    @Test
    public void queLanceExcepcionSiQuiereEliminarUnPlatoQueNoExiste() throws PlatoNoEncontrado {
        Plato plato = this.platosMock.get(0);
        when(repositorioPlato.buscar(anyLong())).thenReturn(null);

        assertThrows(PlatoNoEncontrado.class, () -> servicioPlato.eliminarPlato(plato));
        verify(repositorioPlato, never()).eliminarPlato(plato);
    }
}