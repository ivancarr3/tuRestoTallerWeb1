package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;

public class ControladorHomeTest {

	private ControladorHome controladorHome;
	private ServicioRestaurante servicioRestauranteMock;
	private ServicioPlato servicioPlato;

	@BeforeEach
	public void init() {
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		this.controladorHome = new ControladorHome(this.servicioRestauranteMock, this.servicioPlato);
	}

	@Test
	public void inicioDevuelveHome() {
		ModelAndView modelAndView = controladorHome.inicio();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));

	}

	@Test
	public void QueAlNoBuscarNadaElHomeMeTraigaTodosLosrestaurantes()
			throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500",
				"restaurant.jpg", 100);
		Restaurante restauranteMockeado2 = new Restaurante(null, "El Club de la Milanesa", 5.0, "Arieta 5000",
				"restaurant2.jpg", 100);
		restaurantesMockeados.add(restauranteMockeado1);
		restaurantesMockeados.add(restauranteMockeado2);

		when(servicioRestauranteMock.get()).thenReturn(restaurantesMockeados);

		// ejecucion
		ModelAndView modelAndView = controladorHome.mostrarHome();

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
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500",
				"restaurant.jpg", 100);
		restaurantesMockeados.add(restauranteMockeado1);

		when(servicioRestauranteMock.consultarRestaurantePorNombre(anyString())).thenReturn(restaurantesMockeados);

		// ejecucion
		ModelAndView modelAndView = controladorHome.buscar("La Farola");

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
		restaurantesMock.add(new Restaurante(null, "El club de la Milanesa",
				5.0, "Arieta 5000", "restaurant.jpg", 100));
		restaurantesMock.add(new Restaurante(null, "La Trattoria Bella Italia",
				3.0, "Avenida Libertador 789", "restaurant2.jpg", 100));
		restaurantesMock.add(new Restaurante(null, "La Parrilla de Don Juan",
				4.0, "Avenida Central 456", "restaurant3.jpg", 100));

		when(this.servicioRestauranteMock.get()).thenReturn(restaurantesMock);

		// ejecucion
		ModelAndView mav = this.controladorHome.mostrarHome();

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
		ModelAndView modelAndView = controladorHome.buscar(nombreRestaurante);

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

		ModelAndView modelAndView = controladorHome.buscar(anyString());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("error"),
				equalToIgnoringCase("Error del servidor error"));
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

		ModelAndView modelAndView = controladorHome.filtrar(anyDouble(), anyString());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}

	@Test
	public void elFiltroDeFiltrarNoEncuentraRestaurante()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		when(servicioRestauranteMock.consultarRestaurantePorFiltros(anyDouble(), anyString()))
				.thenThrow(new RestauranteNoEncontrado());

		when(servicioRestauranteMock.get()).thenReturn(listaRestaurante);

		ModelAndView modelAndView = controladorHome.filtrar(anyDouble(), anyString());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("errorFiltro"),
				equalToIgnoringCase("No se encontraron restaurantes"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}

	@Test
	public void elFiltroFallaPorUnErrorDeServidor()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		when(servicioRestauranteMock.consultarRestaurantePorFiltros(anyDouble(), anyString()))
				.thenThrow(new RuntimeException("error"));

		ModelAndView modelAndView = controladorHome.filtrar(anyDouble(), anyString());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("error"),
				equalToIgnoringCase("Error del servidor error"));

	}

	@Test
	public void elFiltroDeFiltrarPorCapacidadDePersonasEncuentraResstaurante()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		List<Restaurante> listaRestaurante = new ArrayList<>();

		Restaurante restauranteMock1 = mock(Restaurante.class);
		Restaurante restauranteMock2 = mock(Restaurante.class);
		Restaurante restauranteMock3 = mock(Restaurante.class);

		listaRestaurante.add(restauranteMock1);
		listaRestaurante.add(restauranteMock2);
		listaRestaurante.add(restauranteMock3);

		when(servicioRestauranteMock.consultarRestaurantePorEspacio(anyInt()))
				.thenReturn(listaRestaurante);

		ModelAndView modelAndView = controladorHome.filtrar(anyInt());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		@SuppressWarnings("unchecked")
		List<Restaurante> restaurantesEnModelo = (List<Restaurante>) modelAndView.getModel().get("restaurantes");
		assertThat(restaurantesEnModelo, containsInAnyOrder(listaRestaurante.toArray()));
	}

	@Test
	public void elFiltroDeFiltrarPorCapacidadDePersonasNoEncuentraResstaurantes()
			throws NoHayRestaurantes {

		when(servicioRestauranteMock.consultarRestaurantePorEspacio(anyInt()))
				.thenThrow(new NoHayRestaurantes());

		ModelAndView modelAndView = controladorHome.filtrar(anyInt());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("errorFiltro"),
				equalToIgnoringCase("No se encontraron restaurantes"));

	}

	@Test
	public void elFiltroPorCapacidadDePersonasFallaPorUnErrorDeServidor()
			throws RestauranteNoEncontrado, NoHayRestaurantes {

		when(servicioRestauranteMock.consultarRestaurantePorEspacio(anyInt()))
				.thenThrow(new RuntimeException("error"));

		ModelAndView modelAndView = controladorHome.filtrar(anyInt());

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));

		assertThat((String) modelAndView.getModel().get("error"),
				equalToIgnoringCase("Error del servidor error"));

	}

}
