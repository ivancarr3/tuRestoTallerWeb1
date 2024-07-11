package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Email;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class ControladorUsuarioRestauranteTest {
    private ControladorUsuarioRestaurante controladorUsuarioRestaurante;
    private ServicioRestaurante servicioRestauranteMock;
    private ServicioReserva servicioReservaMock;
    private ServicioCategoria servicioCategoriaMock;
    private Email emailMock;
    private HttpServletRequest request;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        servicioRestauranteMock = mock(ServicioRestaurante.class);
        servicioReservaMock = mock(ServicioReserva.class);
        servicioCategoriaMock = mock(ServicioCategoria.class);
        emailMock = mock(Email.class);
        request = mock(HttpServletRequest.class);

        this.controladorUsuarioRestaurante = new ControladorUsuarioRestaurante(servicioRestauranteMock, servicioReservaMock, servicioCategoriaMock, emailMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controladorUsuarioRestaurante).build();
    }

    @Test
    public void queAlCargarPerfilRestauranteMuestreReservasYGanancia() throws Exception {
        Long userId = 1L;
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setNombre("Restaurante Prueba");

        List<Reserva> reservas = Arrays.asList(new Reserva(), new Reserva(), new Reserva());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        when(servicioRestauranteMock.consultarPorUsuarioId(userId)).thenReturn(restaurante);
        when(servicioReservaMock.buscarReservasDelRestaurante(restauranteId)).thenReturn(reservas);

        mockMvc.perform(get("/perfil_restaurante").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil_restaurante-home"))
                .andExpect(model().attribute("restauranteNombre", restaurante.getNombre()))
                .andExpect(model().attribute("reservas", reservas))
                .andExpect(model().attribute("ganancias", 15000L)); // 3 reservas * 5000

        verify(servicioRestauranteMock, times(1)).consultarPorUsuarioId(userId);
        verify(servicioReservaMock, times(1)).buscarReservasDelRestaurante(restauranteId);
    }

    @Test
    public void queAlCargarPerfilRestauranteMuestreErrorSiNoHayReservas() throws Exception {
        Long userId = 1L;
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        when(servicioRestauranteMock.consultarPorUsuarioId(userId)).thenReturn(restaurante);
        when(servicioReservaMock.buscarReservasDelRestaurante(restauranteId)).thenThrow(new NoHayReservas());

        mockMvc.perform(get("/perfil_restaurante").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil_restaurante-home"))
                .andExpect(model().attribute("error", "No se encontraron reservas."));

        verify(servicioRestauranteMock, times(1)).consultarPorUsuarioId(userId);
        verify(servicioReservaMock, times(1)).buscarReservasDelRestaurante(restauranteId);
    }

    @Test
    public void queAlCargarPerfilRestauranteMuestreErrorDeServidor() throws Exception {
        Long userId = 1L;
        Long restauranteId = 1L;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        when(servicioRestauranteMock.consultarPorUsuarioId(userId)).thenThrow(new RuntimeException("Error del servidor"));

        mockMvc.perform(get("/perfil_restaurante").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil_restaurante-home"))
                .andExpect(model().attribute("error", "Error del servidor: Error del servidor"));

        verify(servicioRestauranteMock, times(1)).consultarPorUsuarioId(userId);
    }

    @Test
    public void queAlCargarFormPromocionMuestreElFormulario() throws Exception {
        Long restauranteId = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(restauranteId);
        restaurante.setNombre("Restaurante Prueba");

        when(servicioRestauranteMock.consultar(restauranteId)).thenReturn(restaurante);

        mockMvc.perform(get("/perfil_restaurante/{id}/crearPromocion", restauranteId))
                .andExpect(status().isOk())
                .andExpect(view().name("crear_promocion"))
                .andExpect(model().attribute("idRestaurante", restauranteId))
                .andExpect(model().attribute("restaurantId", restauranteId))
                .andExpect(model().attribute("restauranteNombre", restaurante.getNombre()));

        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
    }

    @Test
    public void queAlCargarFormPromocionMuestreErrorSiRestauranteNoEncontrado() throws Exception {
        Long restauranteId = 1L;

        when(servicioRestauranteMock.consultar(restauranteId)).thenThrow(new RestauranteNoEncontrado());

        mockMvc.perform(get("/perfil_restaurante/{id}/crearPromocion", restauranteId))
                .andExpect(status().isOk())
                .andExpect(view().name("crear_promocion"))
                .andExpect(model().attribute("error", "No existe el restaurante"));

        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
    }

    @Test
    public void queAlCargarFormPromocionMuestreErrorDeServidor() throws Exception {
        Long restauranteId = 1L;

        when(servicioRestauranteMock.consultar(restauranteId)).thenThrow(new RuntimeException("Error del servidor"));

        mockMvc.perform(get("/perfil_restaurante/{id}/crearPromocion", restauranteId))
                .andExpect(status().isOk())
                .andExpect(view().name("crear_promocion"))
                .andExpect(model().attribute("error", "Error del servidor: Error del servidor"));

        verify(servicioRestauranteMock, times(1)).consultar(restauranteId);
    }

    @Test
    public void queAlEnviarPromocionSeEnvienLosEmailsCorrectamente() throws Exception {
        Long restauranteId = 1L;
        String subject = "Promocion Especial";
        String text = "Disfruta de un 50% de descuento!";
        List<String> emails = Arrays.asList("test1@example.com", "test2@example.com");

        when(servicioReservaMock.obtenerEmailsUsuariosPorRestaurante(restauranteId)).thenReturn(emails);

        mockMvc.perform(post("/perfil_restaurante/{id}/enviarPromocion", restauranteId)
                        .param("subject", subject)
                        .param("text", text)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("promocion_enviada"))
                .andExpect(model().attribute("message", "Promoción enviada con éxito"))
                .andExpect(model().attribute("restaurantId", restauranteId));

        verify(servicioReservaMock, times(1)).obtenerEmailsUsuariosPorRestaurante(restauranteId);
        verify(emailMock, times(1)).generarEmailPromocionPDF("test1@example.com", subject, text);
        verify(emailMock, times(1)).generarEmailPromocionPDF("test2@example.com", subject, text);
    }

    @Test
    public void queAlEnviarPromocionMuestreErrorSiNoHayReservas() throws Exception {
        Long restauranteId = 1L;
        String subject = "Promocion Especial";
        String text = "Disfruta de un 50% de descuento!";

        when(servicioReservaMock.obtenerEmailsUsuariosPorRestaurante(restauranteId)).thenThrow(new NoHayReservas());

        mockMvc.perform(post("/perfil_restaurante/{id}/enviarPromocion", restauranteId)
                        .param("subject", subject)
                        .param("text", text)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("promocion_enviada"))
                .andExpect(model().attribute("error", "No hay reservas para este restaurante"));

        verify(servicioReservaMock, times(1)).obtenerEmailsUsuariosPorRestaurante(restauranteId);
    }

    @Test
    public void queAlEnviarPromocionMuestreErrorDeServidor() throws Exception {
        Long restauranteId = 1L;
        String subject = "Promocion Especial";
        String text = "Disfruta de un 50% de descuento!";

        when(servicioReservaMock.obtenerEmailsUsuariosPorRestaurante(restauranteId)).thenThrow(new RuntimeException("Error del servidor"));

        mockMvc.perform(post("/perfil_restaurante/{id}/enviarPromocion", restauranteId)
                        .param("subject", subject)
                        .param("text", text)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("promocion_enviada"))
                .andExpect(model().attribute("error", "Error al enviar la promoción: Error del servidor"));

        verify(servicioReservaMock, times(1)).obtenerEmailsUsuariosPorRestaurante(restauranteId);
    }


}
