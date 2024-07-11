package com.tallerwebi.controlador;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorRestauranteTest {

    private ControladorRestaurante controladorRestaurante;
    private ServicioRestaurante servicioRestauranteMock;
    private ServicioPlato servicioPlatoMock;
    private ServicioReserva servicioReservaMock;
    private Email emailMock;
    private HttpServletRequest request;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        servicioRestauranteMock = mock(ServicioRestaurante.class);
        servicioPlatoMock = mock(ServicioPlato.class);
        servicioReservaMock = mock(ServicioReserva.class);
        emailMock = mock(Email.class);
        request = mock(HttpServletRequest.class);

        this.controladorRestaurante = new ControladorRestaurante(servicioRestauranteMock, servicioPlatoMock, servicioReservaMock, emailMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controladorRestaurante).build();
    }

    @Test
    public void queAlEntrarAUnRestauranteMeTraigaLosPlatosCargadosADichoRestaurante() throws Exception {
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Categoria categoriaHamburguesas = new Categoria();
        categoriaHamburguesas.setDescripcion("Hamburguesas");

        Categoria categoriaMilanesas = new Categoria();
        categoriaMilanesas.setDescripcion("Milanesas");

        List<Plato> platos = Arrays.asList(
                new Plato(1L, "Plato 1", 10.0, "Descripción 1", "imagen1.jpg", restaurante, categoriaHamburguesas,
                        true),
                new Plato(2L, "Plato 2", 20.0, "Descripción 2", "imagen2.jpg", restaurante, categoriaMilanesas, false));

        when(servicioRestauranteMock.consultar(restauranteId)).thenReturn(restaurante);
        when(servicioPlatoMock.getPlatosDeRestaurante(restauranteId)).thenReturn(platos);

        mockMvc.perform(get("/restaurante/{id}", restauranteId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurante"))
                .andExpect(model().attributeExists("platosPorCategoria"))
                .andExpect(model().attributeExists("platosRecomendados"))
                .andExpect(model().attributeExists("restaurante"))
                .andExpect(model().attribute("platosRecomendados", hasSize(1)));

        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
        verify(servicioPlatoMock, times(1)).getPlatosDeRestaurante(restauranteId);
    }

    @Test
    public void queAlFiltrarPlatosPorPrecioMeTraigaLosPlatosCorrectamente() throws Exception {
        Long restauranteId = 1L;
        Double precio = 20000.0;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setNombre("Restaurante Prueba");
        Categoria categoria = new Categoria();
        categoria.setDescripcion("Milanesas");

        List<Plato> platos = Arrays.asList(
                new Plato(1L, "Plato 1", 25000.0, "Descripción 1", "imagen1.jpg", restaurante, categoria, true),
                new Plato(2L, "Plato 2", 30000.0, "Descripción 2", "imagen2.jpg", restaurante, categoria, false));

        when(servicioRestauranteMock.consultar(restauranteId)).thenReturn(restaurante);
        when(servicioPlatoMock.consultarPlatoPorPrecio(precio)).thenReturn(platos);

        mockMvc.perform(post("/restaurante/{id}/filtrarPlato", restauranteId)
                        .param("precioPlato", precio.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurante"))
                .andExpect(model().attributeExists("platosPorCategoria"))
                .andExpect(model().attributeExists("platosRecomendados"))
                .andExpect(model().attributeExists("restaurante"))
                .andExpect(model().attribute("platosPorCategoria", hasKey(categoria)))
                .andExpect(model().attribute("platosRecomendados", hasSize(1)))
                .andExpect(model().attribute("platosRecomendados", hasItem(
                        hasProperty("nombre", is("Plato 1"))
                )));

        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
        verify(servicioPlatoMock, times(1)).consultarPlatoPorPrecio(precio);
    }


    @Test
    public void alIntentarMostrarRestauranteElREstauranteNoFueEncontrado()
            throws RestauranteNoEncontrado, NoHayRestaurantes, NoHayPlatos {

        when(servicioRestauranteMock.consultar(1L)).thenThrow(new RestauranteNoEncontrado());

        ModelAndView modelAndView = this.controladorRestaurante.mostrarRestaurante(1L, this.request);

        assertThat((String) modelAndView.getViewName(), equalToIgnoringCase("home"));

        assertThat((String) modelAndView.getModel().get("errorId"),
                equalToIgnoringCase("No se encontró el restaurante"));
    }

    @Test
    public void alIntentarMostrarRestauranteNoencontroPlatosDisponibles()
            throws NoHayPlatos, RestauranteNoEncontrado, NoHayRestaurantes {

        Restaurante resto = mock(Restaurante.class);

        when(servicioPlatoMock.getPlatosDeRestaurante(1L)).thenThrow(new NoHayPlatos());

        when(servicioRestauranteMock.consultar(1L)).thenReturn(resto);

        ModelAndView modelAndView = this.controladorRestaurante.mostrarRestaurante(1L, this.request);

        assertThat((String) modelAndView.getViewName(), equalToIgnoringCase("restaurante"));

        assertThat((String) modelAndView.getModel().get("error"),
                equalToIgnoringCase("No hay platos en este restaurante"));

    }

    @Test
    public void alIntentarMostrarRestauranteOcurreErrorDeServidor()
            throws NoHayPlatos, RestauranteNoEncontrado, NoHayRestaurantes {

        when(servicioRestauranteMock.consultar(1L)).thenThrow(new RuntimeException("error"));

        ModelAndView modelAndView = this.controladorRestaurante.mostrarRestaurante(1L, this.request);

        assertThat((String) modelAndView.getViewName(), equalToIgnoringCase("home"));

        assertThat((String) modelAndView.getModel().get("error"),
                equalToIgnoringCase("Error del servidor: error"));

    }

    @Test
    public void filtrarPlatoNoEncuentraPlato() throws RestauranteNoEncontrado, PlatoNoEncontrado {

        Restaurante restomock = mock(Restaurante.class);

        when(servicioRestauranteMock.consultar(1L)).thenReturn(restomock);

        when(servicioPlatoMock.consultarPlatoPorPrecio(anyDouble())).thenThrow(new PlatoNoEncontrado());

        ModelAndView modelAndView = controladorRestaurante.filtrarPlato(1L, "3", this.request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("restaurante"));

        assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("No existen platos"));

    }

    @Test
    public void queLanceExcepcionGeneralAlFiltrarPlatoSiHayErrorEnServidor() throws RestauranteNoEncontrado, PlatoNoEncontrado {

        Restaurante restomock = mock(Restaurante.class);

        when(servicioRestauranteMock.consultar(1L)).thenReturn(restomock);

        when(servicioPlatoMock.consultarPlatoPorPrecio(anyDouble())).thenThrow(new RuntimeException("errorTest"));

        ModelAndView modelAndView = controladorRestaurante.filtrarPlato(1L, "3", this.request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("restaurante"));

        assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("Error del servidor: errorTest"));

    }

    @Test
    public void filtrarPlatoNoEncuentraRestaurante() throws RestauranteNoEncontrado, PlatoNoEncontrado {

        when(servicioRestauranteMock.consultar(1L)).thenThrow(new RestauranteNoEncontrado());

        ModelAndView modelAndView = controladorRestaurante.filtrarPlato(1L, "3", this.request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("restaurante"));

        assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("No existe el restaurante"));

    }

    @Test
    public void filtrarLanzaUnErrorDeServidor() throws RestauranteNoEncontrado, PlatoNoEncontrado {

        when(servicioRestauranteMock.consultar(1L)).thenThrow(new RestauranteNoEncontrado());

        ModelAndView modelAndView = controladorRestaurante.filtrarPlato(1L, "3", this.request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("restaurante"));

        assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("No existe el restaurante"));
    }


}
