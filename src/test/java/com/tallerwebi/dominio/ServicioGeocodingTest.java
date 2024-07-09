package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioGeocodingTest {

    private ServicioGeocoding servicioGeocoding;

    @BeforeEach
    public void setUp() {
        servicioGeocoding = new ServicioGeocoding();
    }

    @Test
    public void testObtenerCoordenadasExito() {
        String direccion = "1600 Amphitheatre Parkway, Mountain View, CA";
        ServicioGeocoding.Coordenadas coordenadas = servicioGeocoding.obtenerCoordenadas(direccion);

        assertNotNull(coordenadas, "Las coordenadas no deberían ser nulas");
        assertEquals(37.422, coordenadas.getLatitud(), 0.001, "La latitud no coincide");
        assertEquals(-122.084, coordenadas.getLongitud(), 0.001, "La longitud no coincide");
    }

}
