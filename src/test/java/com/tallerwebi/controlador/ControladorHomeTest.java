package com.tallerwebi.controlador;


import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.infraestructura.RepositorioRestauranteImpl;
import com.tallerwebi.servicio.ServicioHome;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.hsqldb.SchemaObjectSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorHomeTest {
	
	private ControladorHome controladorHome;
	private ServicioRestaurante servicioRestauranteMock;
	
	
	@BeforeEach
	public void init(){
		
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		controladorHome = new ControladorHome(servicioRestauranteMock);
	}

	@Test
	public void QueAlNoBuscarNadaElHomeMeTraigaTodosLosrestaurantes() throws RestauranteNoEncontrado{
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500", "");
		Restaurante restauranteMockeado2 = new Restaurante(null, "El Club de la Milanesa", 5.0, "Arieta 5000", "");
		restaurantesMockeados.add(restauranteMockeado1);
		restaurantesMockeados.add(restauranteMockeado2);
		
		when(servicioRestauranteMock.get()).thenReturn(restaurantesMockeados);

		// ejecucion
		ModelAndView modelAndView = controladorHome.mostrarHome();

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("Home"));
		List<Restaurante> restaurantesModel = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertEquals(2, restaurantesModel.size());
	}
	
	@Test
	public void QueAlBuscarUnRestaurantePorNombreQueExistaMeMuestreEseRestaurante() throws RestauranteNoEncontrado {
		//preparacion 
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500", "");
		restaurantesMockeados.add(restauranteMockeado1);
		
		when(servicioRestauranteMock.consultarRestaurantePorNombre(anyString())).thenReturn(restaurantesMockeados);
		
		//ejecucion
		ModelAndView modelAndView = controladorHome.buscar("La Farola");
		
		//validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("Home"));
		List<Restaurante> restaurantesModel = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertEquals(1, restaurantesModel.size());
		assertEquals("La Farola", restaurantesModel.get(0).getNombre());
		assertEquals(4.0, restaurantesModel.get(0).getEstrellas());
		assertEquals("Santa Maria 3500", restaurantesModel.get(0).getDireccion());

	}
	

}
