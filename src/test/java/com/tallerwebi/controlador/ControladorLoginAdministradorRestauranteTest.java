package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.AdministradorDeRestaurante;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.servicio.ServicioLoginAdministradorDeRestaurante;
import com.tallerwebi.servicio.ServicioLoginAdministradorDeRestaurante;

public class ControladorLoginAdministradorRestauranteTest {
    private ControladorLoginAdministradorRestaurante controladorLoginAdministradorRestaurante;
    private AdministradorDeRestaurante administradorDeRestaurante;
    private DatosLogin datosLoginMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioLoginAdministradorDeRestaurante servicioLoginAdministradorRestaurante;

    @BeforeEach
    public void init() {
        datosLoginMock = new DatosLogin("ad@ad.com", "123");
        administradorDeRestaurante = mock(AdministradorDeRestaurante.class);
        when(administradorDeRestaurante.getEmail()).thenReturn("ad@ad.com");
        when(administradorDeRestaurante.getPassword()).thenReturn("pass");
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioLoginAdministradorRestaurante = mock(ServicioLoginAdministradorDeRestaurante.class);
        controladorLoginAdministradorRestaurante = new ControladorLoginAdministradorRestaurante(
                servicioLoginAdministradorRestaurante);
    }

    @Test
    public void irdAlLoginDeAdministradorDevuelveLaVistaCorrespondiente() {

        ModelAndView modelAndView = controladorLoginAdministradorRestaurante.irAlLoginAdministrador();

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("admin_restaurante_login"));

    }

    @Test
    public void validarLoginAdministradorExitosoMeRedirigeAVistaMiResto() {

        String email = "adm@adm.com";
        String pass = "pass";
        Restaurante restaurante = null;
        DatosLogin datosLogin = mock(DatosLogin.class);
        when(datosLogin.getEmail()).thenReturn(email);
        when(datosLogin.getPassword()).thenReturn(pass);

        AdministradorDeRestaurante administradorDeRestaurante = new AdministradorDeRestaurante(123L, "adm@adm.com",
                "pass", restaurante);

        when(servicioLoginAdministradorRestaurante.buscarAdministradorDeRestaurante(email, pass))
                .thenReturn(administradorDeRestaurante);

        when(requestMock.getSession()).thenReturn(sessionMock);

        ModelAndView modelAndView = controladorLoginAdministradorRestaurante.validarLoginAdministrador(datosLogin,
                requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/mi-resto"));

    }

    @Test
    public void validarLoginNoEncuentraUsuarioExistenteVuelveAlLoginYDevuelveMensajeDeError() {
        when(servicioLoginAdministradorRestaurante.buscarAdministradorDeRestaurante(anyString(), anyString()))
                .thenReturn(null);

        ModelAndView modelAndView = controladorLoginAdministradorRestaurante.validarLoginAdministrador(datosLoginMock,
                requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("admin_restaurante_login"));

        assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("usuario no encontrado"));

    }
}
