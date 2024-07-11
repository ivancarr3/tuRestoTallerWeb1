package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioPlatoTest {

    private static final Long EXISTING_ID = 1L;
    private static final String MILANESA = "milanesa";
    private static final String MILANESA_CARNE = "milanesa de carne";
    private static final String POLLO = "pollo";
    private static final String ASADO = "asado";
    private static final String MILANESA_POLLO = "milanesa de pollo";
    private static final double PRICE_20000 = 20000.0;
    private static final double PRICE_10000 = 10000.0;
    private static final double PRICE_17000 = 17000.0;
    private static final double PRICE_9000 = 9000.0;

    private ServicioPlato servicioPlato;
    private RepositorioPlato repositorioPlato;
    private ServicioRestaurante servicioRestaurante;
    private RepositorioRestaurante repositorioRestaurante;
    private List<Plato> platosMock;
    private ServicioReserva servicioReserva;
    private Categoria categoriaMock;
    private ServicioGeocoding servicioGeocoding;

    @BeforeEach
    public void init(){
        initializeMocks();
        initializePlatosMock();
        this.servicioPlato = new ServicioPlatoImpl(this.repositorioPlato);
        this.servicioRestaurante = new ServicioRestauranteImpl(this.repositorioRestaurante, this.servicioReserva, this.servicioGeocoding);
    }

    private void initializeMocks() {
        this.repositorioPlato = mock(RepositorioPlato.class);
        this.servicioReserva = mock(ServicioReserva.class);
        this.repositorioRestaurante = mock(RepositorioRestaurante.class);
        this.servicioGeocoding = mock(ServicioGeocoding.class);
        this.categoriaMock = mock(Categoria.class);
    }

    private void initializePlatosMock() {
        Restaurante resto1 = new Restaurante(1L, "El club de la milanesa", 4.0, "", "", 2, -34.610000, -58.400000, true, new Usuario());
        Restaurante resto2 = new Restaurante(2L, "Mundo Milanesa", 3.0, "", "", 2, -34.610000, -58.400000, true, new Usuario());
        this.platosMock = new ArrayList<>();
        this.platosMock.add(new Plato(1L, MILANESA_CARNE, PRICE_20000, "napolitana", "", resto1, categoriaMock, true));
        this.platosMock.add(new Plato(2L, POLLO, PRICE_10000, "a la mostaza", "", resto1, categoriaMock, true));
        this.platosMock.add(new Plato(3L, ASADO, PRICE_17000, "con hueso", "", resto2, categoriaMock, true));
        this.platosMock.add(new Plato(4L, MILANESA_POLLO, PRICE_17000, "con queso", "", resto2, categoriaMock, true));
    }

    @Test
    public void queSePuedanObtenerTodosLosPlatos() throws NoHayPlatos {
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
    public void queSePuedaObtenerUnPlatoPorId() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscar(EXISTING_ID)).thenReturn(this.platosMock.get(0));

        Plato plato = this.servicioPlato.consultar(EXISTING_ID);

        assertEquals(plato.getNombre(), this.platosMock.get(0).getNombre());
    }

    @Test
    public void queAlNoEncontrarPlatoPorIdLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscar(EXISTING_ID)).thenReturn(null);

        assertThrows(PlatoNoEncontrado.class, () -> this.servicioPlato.consultar(EXISTING_ID));
    }

    @Test
    public void queAlBuscarPlatosPorNombreDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        List<Plato> platosPorNombre = List.of(
                this.platosMock.get(0), // Primer elemento
                this.platosMock.get(this.platosMock.size() - 1) // Último elemento
        );
        when(this.repositorioPlato.buscarPlatoPorNombre(MILANESA)).thenReturn(platosPorNombre);

        List<Plato> platos = this.servicioPlato.consultarPlatoPorNombre(MILANESA);

        assertThat(platos.size(), equalTo(2));
    }

    @Test
    public void queAlNoEncontrarPlatosPorNombreLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscarPlatoPorNombre(MILANESA)).thenReturn(this.platosMock);

        assertThrows(PlatoNoEncontrado.class, () -> this.servicioPlato.consultarPlatoPorNombre(ASADO));
    }

    @Test
    public void queAlBuscarPlatosPorPrecioDevuelvaLosCorrespondientes() throws PlatoNoEncontrado {
        List<Plato> platoPorPrecio = List.of(this.platosMock.get(0));
        when(this.repositorioPlato.buscarPlatoPorPrecio(PRICE_20000)).thenReturn(platoPorPrecio);

        List<Plato> platos = this.servicioPlato.consultarPlatoPorPrecio(PRICE_20000);

        assertThat(platos.size(), equalTo(1));
    }

    @Test
    public void queAlNoEncontrarPlatosPorPrecioLanceExcepcion() throws PlatoNoEncontrado {
        when(this.repositorioPlato.buscarPlatoPorPrecio(PRICE_20000)).thenReturn(this.platosMock);

        assertThrows(PlatoNoEncontrado.class, () -> this.servicioPlato.consultarPlatoPorPrecio(15000.0));
    }

    @Test
    public void queOrdenePlatosPorPrecioAscendente() throws NoHayPlatos {
        Collections.sort(this.platosMock, Comparator.comparingDouble(Plato::getPrecio));
        when(this.repositorioPlato.ordenarPorPrecio("ASC")).thenReturn(this.platosMock);

        List<Plato> platos = this.servicioPlato.ordenarPorPrecio("ASC");

        assertThat(platos.size(), equalTo(4));
        for (int i = 0; i < platos.size() - 1; i++) {
            assertThat(platos.get(i).getPrecio(), lessThanOrEqualTo(platos.get(i + 1).getPrecio()));
        }
    }

    @Test
    public void queOrdenePlatosPorPrecioDescendente() throws NoHayPlatos {
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
        Plato nuevoPlato = new Plato(5L, "sopa", PRICE_9000, "con lentejas", "", new Restaurante(), new Categoria(), true);

        servicioPlato.crearPlato(nuevoPlato);

        verify(repositorioPlato, times(1)).guardarPlato(nuevoPlato);
    }

    @Test
    public void queLanceExcepcionSiSeCreaUnPlatoConElMismoId() throws PlatoExistente {
        Plato platoExistente = new Plato(5L, "sopa", PRICE_9000, "con lentejas", "", new Restaurante(), new Categoria(), true);

        when(repositorioPlato.buscar(5L)).thenReturn(platoExistente);

        Plato nuevoPlato = new Plato(5L, "sopa", PRICE_9000, "con lentejas", "", new Restaurante(), new Categoria(), true);

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
        Plato plato = new Plato(56L, "sopa", PRICE_9000, "con lentejas", "", new Restaurante(), new Categoria(), true);

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

    @Test
    public void queAlObtenerPlatosDeRestauranteLoConsiga() throws NoHayPlatos {

        List<Plato> listaDePlatos = new ArrayList<>();

        Plato plato1 = mock(Plato.class);
        Plato plato2 = mock(Plato.class);
        Plato plato3 = mock(Plato.class);

        listaDePlatos.add(plato3);
        listaDePlatos.add(plato2);
        listaDePlatos.add(plato1);

        when(repositorioPlato.getPlatosDeRestaurante(anyLong())).thenReturn(listaDePlatos);

        ArrayList<Plato> platos = (ArrayList<Plato>) servicioPlato.getPlatosDeRestaurante(anyLong());

        assertThat(platos, containsInAnyOrder(listaDePlatos.toArray()));

    }

    @Test
    public void queAlObtenerPlatosDeRestauranteNoExistanPlatos() {

        when(repositorioPlato.getPlatosDeRestaurante(anyLong())).thenReturn(null);

        try {
            servicioPlato.getPlatosDeRestaurante(anyLong());
        } catch (NoHayPlatos e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void queAlAgruparPorCategoriaMeDevuelvaUnArray() {
        List<Plato> listaDePlatos = new ArrayList<>();

        Plato plato1 = mock(Plato.class);
        Plato plato2 = mock(Plato.class);
        Plato plato3 = mock(Plato.class);

        listaDePlatos.add(plato3);
        listaDePlatos.add(plato2);
        listaDePlatos.add(plato1);

        when(repositorioPlato.getPlatosAgrupadosPorCategoria()).thenReturn(listaDePlatos);

        ArrayList<Plato> platos = (ArrayList<Plato>) servicioPlato.getPlatosAgrupadosPorCategoria();

        assertThat(platos, containsInAnyOrder(listaDePlatos.toArray()));
    }

    @Test
    public void queAlOrdenarPorPrecioNoExistanPlatos() {
        when(repositorioPlato.ordenarPorPrecio(anyString())).thenReturn(null);
        try {
            servicioPlato.ordenarPorPrecio(anyString());
        } catch (NoHayPlatos e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void queSePuedanObtenerPlatosPorCategoria() throws NoHayPlatos {
        List<Plato> listaDePlatos = new ArrayList<>();

        Plato plato1 = mock(Plato.class);
        Plato plato2 = mock(Plato.class);
        Plato plato3 = mock(Plato.class);

        listaDePlatos.add(plato3);
        listaDePlatos.add(plato2);
        listaDePlatos.add(plato1);

        when(repositorioPlato.getPlatosPorCategoria("categoria")).thenReturn(listaDePlatos);

        List<Plato> platos = servicioPlato.getPlatosPorCategoria("categoria");

        assertEquals(platos, listaDePlatos);
    }

    @Test
    public void queLanceExcepcionSiNoHayPlatosPorCategoria() throws NoHayPlatos {
        when(repositorioPlato.getPlatosPorCategoria("categoria")).thenReturn(null);

        assertThrows(NoHayPlatos.class, () -> servicioPlato.getPlatosPorCategoria("categoria"));
    }
}
