package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ControladorAdminRestauranteTest {

    @Mock
    private ServicioRestaurante servicioRestaurante;

    @InjectMocks
    private ControladorAdminRestaurantes controladorAdminRestaurantes;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorAdminRestaurantes).build();
    }

    @Test
    public void testHomeAdministradorRestaurantes() throws Exception {
        List<Restaurante> restaurantesHabilitados = Arrays.asList(new Restaurante(), new Restaurante());
        List<Restaurante> restaurantesDeshabilitados = Arrays.asList(new Restaurante(), new Restaurante());

        when(servicioRestaurante.obtenerRestaurantesHabilitados()).thenReturn(restaurantesHabilitados);
        when(servicioRestaurante.obtenerRestaurantesDeshabilitados()).thenReturn(restaurantesDeshabilitados);

        mockMvc.perform(get("/admin-restaurantess"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-restaurantes"))
                .andExpect(model().attribute("restaurantesHabilitados", restaurantesHabilitados))
                .andExpect(model().attribute("restaurantesDeshabilitados", restaurantesDeshabilitados));

        verify(servicioRestaurante, times(1)).obtenerRestaurantesHabilitados();
        verify(servicioRestaurante, times(1)).obtenerRestaurantesDeshabilitados();
    }

    @Test
    public void testHabilitarRestaurante() throws Exception {
        Long idRestaurante = 1L;

        mockMvc.perform(post("/restaurantes/habilitar").param("id", String.valueOf(idRestaurante)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-restaurantes"));

        verify(servicioRestaurante, times(1)).habilitarRestaurante(idRestaurante);
    }

    @Test
    public void testDeshabilitarRestaurante() throws Exception {
        Long idRestaurante = 1L;

        mockMvc.perform(post("/restaurantes/deshabilitar").param("id", String.valueOf(idRestaurante)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-restaurantes"));

        verify(servicioRestaurante, times(1)).deshabilitarRestaurante(idRestaurante);
    }

    @Test
    public void testEliminarRestaurante() throws Exception {
        Long idRestaurante = 1L;

        mockMvc.perform(post("/restaurantes/eliminar").param("id", String.valueOf(idRestaurante)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-restaurantes"));

        verify(servicioRestaurante, times(1)).eliminarRestaurantePorId(idRestaurante);
    }
}
