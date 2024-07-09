package com.tallerwebi.dominio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public class ServicioMercadoPagoTest {

    @InjectMocks
    private ServicioMercadoPagoImpl servicioMercadoPago;

    @Mock
    private Restaurante restaurante;

    @Mock
    private Reserva reserva;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(restaurante.getNombre()).thenReturn("Restaurante de Prueba");
        when(restaurante.getDireccion()).thenReturn("Calle Falsa 123");
        when(restaurante.getImagen()).thenReturn("http://imagen.com/imagen.jpg");

        when(reserva.getFechaFormateada()).thenReturn(LocalDateTime.now().toString());
    }

    @Test
    public void testArmarPago() throws MPException, MPApiException {
        String preferenciaId = servicioMercadoPago.armarPago(restaurante, reserva, 100.0);

        assertNotNull(preferenciaId, "La preferencia no debería ser nula");
    }
}
