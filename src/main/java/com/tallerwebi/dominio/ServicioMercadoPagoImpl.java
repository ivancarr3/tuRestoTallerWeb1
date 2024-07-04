package com.tallerwebi.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPDefaultHttpClient;
import com.mercadopago.resources.preference.Preference;
import com.tallerwebi.servicio.ServicioMercadoPago;

@Service
public class ServicioMercadoPagoImpl implements ServicioMercadoPago {

	public String armarPago(Restaurante restaurante, Reserva reserva, double total) throws MPException, MPApiException {
		MercadoPagoConfig.setAccessToken("TEST-231726774127987-061717-be04919a5b6480ff7181884b074dd77c-423795726");

		String desc = "Reserva en " + restaurante.getNombre() + ". Direccion: " + restaurante.getDireccion()
		+ ". Fecha: " + reserva.getFechaFormateada();
		
		PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
				.id("1234")
				.title(restaurante.getNombre())
				.description(desc)
				.pictureUrl(restaurante.getImagen())
				.categoryId("food")
				.quantity(1)
				.currencyId("ARS")
				.unitPrice(new BigDecimal(total))
				.build();

		List<PreferenceItemRequest> items = new ArrayList<>();
		items.add(itemRequest);
		PreferenceRequest preferenceRequest = PreferenceRequest.builder().items(items).build();
		PreferenceClient client = new PreferenceClient(new MPDefaultHttpClient());
		Preference preference = client.create(preferenceRequest);
		return preference.getId();
	}
}
