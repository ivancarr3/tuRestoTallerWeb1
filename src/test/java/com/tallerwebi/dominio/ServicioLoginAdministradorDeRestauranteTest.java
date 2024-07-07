package com.tallerwebi.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioLoginAdministradorDeRestauranteTest {

    RepositorioAdministradorRestaurante repositorioAdministradorRestaurante;
    AdministradorDeRestaurante administradorDeRestaurante;

    @BeforeEach
    public void init() {
        repositorioAdministradorRestaurante = mock(RepositorioAdministradorRestaurante.class);

        administradorDeRestaurante = mock(AdministradorDeRestaurante.class);
        when(administradorDeRestaurante.getEmail()).thenReturn("test@tes.com");

    }

    @Test
    public void alBuscarAdministradorDeRestauranteLoEncuentraYLoDevuelve() {
        when(repositorioAdministradorRestaurante.buscarAdministradorDeRestaurante(anyString(), anyString()))
                .thenReturn(administradorDeRestaurante);

        AdministradorDeRestaurante adminDevuelto = repositorioAdministradorRestaurante
                .buscarAdministradorDeRestaurante(anyString(), anyString());

        assertThat(adminDevuelto.getEmail(), equalToIgnoringCase("test@tes.com"));
    }

}
