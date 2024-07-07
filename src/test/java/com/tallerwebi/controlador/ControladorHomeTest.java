package com.tallerwebi.controlador;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.ServicioGeocoding;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;

public class ControladorHomeTest {

	private ControladorHome controladorHome;
	private ServicioRestaurante servicioRestauranteMock;
	private ServicioPlato servicioPlato;
	private ServicioGeocoding servicioGeocoding;
	private HttpServletRequest request;

	@BeforeEach
	public void init() {
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		servicioPlato = mock(ServicioPlato.class);
		this.controladorHome = new ControladorHome(this.servicioRestauranteMock, this.servicioPlato,
				this.servicioGeocoding);
		this.request = mock(HttpServletRequest.class);
	}

	@Test
	public void inicioDevuelveHome() {
		ModelAndView modelAndView = controladorHome.inicio();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));

	}

	@Test
	public void filtrarPlatosCategoriaDevuelveVistaCorrectaConPlatos() throws NoHayPlatos {
		String categoria = "pizza";
		List<Plato> platosMockeados = new ArrayList<>();
		platosMockeados.add(new Plato(1L, "Pizza Margarita", 10.0, "Delicious pizza with mozzarella and basil", "pizza1.jpg", null, null, false));
		platosMockeados.add(new Plato(2L, "Pizza Pepperoni", 12.0, "Spicy pepperoni pizza", "pizza2.jpg", null, null, false));
		platosMockeados.add(new Plato(3L, "Pizza Hawaiana", 11.0, "Pizza with pineapple and ham", "pizza3.jpg", null, null, false));
		platosMockeados.add(new Plato(4L, "Pizza Cuatro Quesos", 13.0, "Pizza with four types of cheese", "pizza4.jpg", null, null, false));

		when(servicioPlato.getPlatosPorCategoria(categoria)).thenReturn(platosMockeados);

		ModelAndView modelAndView = controladorHome.filtrarPlatosCategoria(categoria, this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));
		@SuppressWarnings("unchecked")
		List<Plato> platosModel = (List<Plato>) modelAndView.getModel().get("platos");
		assertEquals(4, platosModel.size());
		assertEquals("Pizza Margarita", platosModel.get(0).getNombre());
		assertEquals("Pizza Pepperoni", platosModel.get(1).getNombre());
		assertEquals("Pizza Hawaiana", platosModel.get(2).getNombre());
		assertEquals("Pizza Cuatro Quesos", platosModel.get(3).getNombre());
	}

	@Test
	public void filtrarPlatosCategoriaSinPlatosLanzaExcepcion() throws NoHayPlatos {
		String categoria = "noexiste";
		when(servicioPlato.getPlatosPorCategoria(categoria)).thenThrow(new NoHayPlatos());

		ModelAndView modelAndView = controladorHome.filtrarPlatosCategoria(categoria, this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("Home"));
		assertEquals("No hay platos disponibles para esa categoría.", modelAndView.getModel().get("error"));
	}

	@Test
	public void QueAlNoBuscarNadaElHomeMeTraigaTodosLosrestaurantes()
			throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500", "restaurant.jpg",
				100, -34.598940, -58.415550);
		Restaurante restauranteMockeado2 = new Restaurante(null, "El Club de la Milanesa", 5.0, "Arieta 5000",
				"restaurant2.jpg", 100, -34.598940, -58.415550);
		restaurantesMockeados.add(restauranteMockeado1);
		restaurantesMockeados.add(restauranteMockeado2);

		when(servicioRestauranteMock.get()).thenReturn(restaurantesMockeados);

		// ejecucion
		ModelAndView modelAndView = controladorHome.mostrarHome(this.request);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("Home"));
		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesModel = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertEquals(2, restaurantesModel.size());
	}

	@Test
	public void QueAlBuscarUnRestaurantePorNombreQueExistaMeMuestreEseRestaurante()
			throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500", "restaurant.jpg",
				100, -34.598940, -58.415550);
		restaurantesMockeados.add(restauranteMockeado1);

		when(servicioRestauranteMock.consultarRestaurantePorNombre(anyString())).thenReturn(restaurantesMockeados);

		// ejecucion
		ModelAndView modelAndView = controladorHome.buscar("La Farola", request);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("Home"));
		List<Restaurante> restaurantesModel = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertEquals(1, restaurantesModel.size());
		assertEquals("La Farola", restaurantesModel.get(0).getNombre());
		assertEquals(4.0, restaurantesModel.get(0).getEstrellas());
		assertEquals("Santa Maria 3500", restaurantesModel.get(0).getDireccion());
	}

	@Test
	public void queAlIngresarALaPantallaHomeMuestreTodosLosRestaurantesExistentes() throws NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMock = new ArrayList<Restaurante>();

		restaurantesMock.add(new Restaurante(null, "El club de la Milanesa", 5.0, "Arieta 5000", "restaurant.jpg", 100,
				-34.598940, -58.415550));
		restaurantesMock.add(new Restaurante(null, "La Trattoria Bella Italia", 3.0, "Avenida Libertador 789",
				"restaurant2.jpg", 100, -34.598940, -58.415550));
		restaurantesMock.add(new Restaurante(null, "La Parrilla de Don Juan", 4.0, "Avenida Central 456",
				"restaurant3.jpg", 100, -34.598940, -58.415550));

		when(this.servicioRestauranteMock.get()).thenReturn(restaurantesMock);

		// ejecucion
		ModelAndView mav = this.controladorHome.mostrarHome(this.request);

		// verificacion
		List<Restaurante> restaurantes = (List<Restaurante>) mav.getModel().get("restaurantes");
		assertEquals(3, restaurantes.size());
	}

	@Test
	public void alBuscarNoEncuentraRestaurante() throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparación
		List<Restaurante> listaRestoVacia = new ArrayList<>();
		String nombreRestaurante = "Papirola";

		when(servicioRestauranteMock.consultarRestaurantePorNombre(nombreRestaurante))
				.thenThrow(new RestauranteNoEncontrado());

		when(servicioRestauranteMock.get()).thenReturn(listaRestoVacia);

		// ejecución
		ModelAndView modelAndView = controladorHome.buscar(nombreRestaurante, this.request);

		// validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("errorBusqueda"),
				equalToIgnoringCase("No se encontraron restaurantes con el nombre Papirola"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestoVacia.toArray()));
	}

	@Test
	public void alBuscarFallaPorErrorEnServidor() throws RestauranteNoEncontrado, NoHayRestaurantes {
		when(servicioRestauranteMock.consultarRestaurantePorNombre(anyString()))
				.thenThrow(new RuntimeException("error"));

		ModelAndView modelAndView = controladorHome.buscar(anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("Error del servidor: error"));
	}

	@Test
	public void elFiltroDeFiltrarFuncionaBienYMeDevuelveUnaVistaYUnModeloCorrecto()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();
		try {
			when(servicioRestauranteMock.consultarRestaurantePorFiltros(anyDouble(), anyString()))
					.thenReturn(listaRestaurante);
		} catch (RestauranteNoEncontrado e) {
			fail("No se esperaba una excepción al llamar al método: " + e.getMessage());
		}

		ModelAndView modelAndView = controladorHome.filtrar(anyDouble(), anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}

	@Test
	public void elFiltroDeFiltrarNoEncuentraRestaurante() throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		when(servicioRestauranteMock.consultarRestaurantePorFiltros(anyDouble(), anyString()))
				.thenThrow(new RestauranteNoEncontrado());

		when(servicioRestauranteMock.get()).thenReturn(listaRestaurante);

		ModelAndView modelAndView = controladorHome.filtrar(any(), anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));



		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}

	@Test
	public void elFiltroFallaPorUnErrorDeServidor() throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		when(servicioRestauranteMock.consultarRestaurantePorFiltros(anyDouble(), anyString()))
				.thenThrow(new RuntimeException("error"));

		ModelAndView modelAndView = controladorHome.filtrar(anyDouble(), anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("error"), equalToIgnoringCase("Error del servidor: error"));

	}

	/*@Test
	public void elFiltroDeFiltrarPorCapacidadDePersonasEncuentraResstaurante()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		Restaurante restauranteMock1 = mock(Restaurante.class);
		Restaurante restauranteMock2 = mock(Restaurante.class);
		Restaurante restauranteMock3 = mock(Restaurante.class);

		listaRestaurante.add(restauranteMock1);
		listaRestaurante.add(restauranteMock2);
		listaRestaurante.add(restauranteMock3);

		when(servicioRestauranteMock.consultarRestaurantePorEspacio(anyInt())).thenReturn(listaRestaurante);

		ModelAndView modelAndView = controladorHome.filtrar(any(), anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}*/

	@Test
	public void elFiltroDeFiltrarPorCapacidadDePersonasNoEncuentraRestaurantes() throws NoHayRestaurantes {

		when(servicioRestauranteMock.consultarRestaurantePorEspacio(anyInt())).thenThrow(new NoHayRestaurantes());

		ModelAndView modelAndView = controladorHome.filtrar(any(), anyString(), this.request);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));
	}
}
