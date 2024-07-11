package com.tallerwebi.servicio;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;

import java.util.Map;

public interface ServicioMercadoPago {

	String armarPago(Restaurante restaurante, Reserva reserva, double total) throws MPException, MPApiException;
}
