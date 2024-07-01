package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

public class ControladorInfoRestauranteTest {

    ControladorInforestaurante controladorInforestaurante;

    @BeforeEach
    public void init() {
        controladorInforestaurante = new ControladorInforestaurante();
    }

    @Test
    public void infoRestauranteDevuelveSuVista() {
        ModelAndView modelAndView = controladorInforestaurante.inicio();

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("inforestaurante"));
    }

}
