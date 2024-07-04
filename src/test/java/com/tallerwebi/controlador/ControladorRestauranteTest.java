package com.tallerwebi.controlador;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorRestauranteTest {

    private ControladorRestaurante controladorRestaurante;
    private ServicioRestaurante servicioRestauranteMock;
    private ServicioPlato servicioPlatoMock;
    private ServicioReserva servicioReservaMock;
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        servicioRestauranteMock = mock(ServicioRestaurante.class);
        servicioPlatoMock = mock(ServicioPlato.class);
        servicioReservaMock = mock(ServicioReserva.class);
        
        this.controladorRestaurante = new ControladorRestaurante(servicioRestauranteMock, servicioPlatoMock, servicioReservaMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controladorRestaurante).build();
    }

    @Test
    public void queAlEntrarAUnRestauranteMeTraigaLosPlatosCargadosADichoRestaurante() throws Exception{
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        Categoria categoriaHamburguesas = new Categoria();
        categoriaHamburguesas.setDescripcion("Hamburguesas");

        Categoria categoriaMilanesas = new Categoria();
        categoriaMilanesas.setDescripcion("Milanesas");

        List<Plato> platos = Arrays.asList(
                new Plato(1L, "Plato 1", 10.0, "Descripción 1", "imagen1.jpg", restaurante, categoriaHamburguesas, true),
                new Plato(2L, "Plato 2", 20.0, "Descripción 2", "imagen2.jpg", restaurante, categoriaMilanesas, false)
        );

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
        Categoria categoria = new Categoria();
        categoria.setDescripcion("Milanesas");

        List<Plato> platos = Arrays.asList(
                new Plato(1L, "Plato 1", 25000.0, "Descripción 1", "imagen1.jpg", restaurante, categoria, true),
                new Plato(2L, "Plato 2", 30000.0, "Descripción 2", "imagen2.jpg", restaurante, categoria, false)
        );

        when(servicioRestauranteMock.consultar(restauranteId)).thenReturn(restaurante);
        when(servicioPlatoMock.consultarPlatoPorPrecio(precio)).thenReturn(platos);

        mockMvc.perform(post("/restaurante/filtrarPlato")
                        .param("idRestaurante", restauranteId.toString())
                        .param("precioPlato", precio.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurante"))
                .andExpect(model().attributeExists("platosPorCategoria"))
                .andExpect(model().attributeExists("platosRecomendados"))
                .andExpect(model().attributeExists("restaurante"))
                .andExpect(model().attribute("platosPorCategoria", hasKey(categoria)))
                .andExpect(model().attribute("platosRecomendados", hasSize(1)));


        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
        verify(servicioPlatoMock, times(1)).consultarPlatoPorPrecio(precio);
    }
}
