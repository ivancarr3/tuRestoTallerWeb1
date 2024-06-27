package com.tallerwebi.controlador;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.ServicioGeocoding;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
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
	private ServicioGeocoding servicioGeocoding;


	@BeforeEach
	public void init() {
		servicioRestauranteMock = mock(ServicioRestaurante.class);
		this.controladorHome = new ControladorHome(this.servicioRestauranteMock, this.servicioPlato, this.servicioGeocoding);
	}

	@Test
	public void QueAlNoBuscarNadaElHomeMeTraigaTodosLosrestaurantes() throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500",
				"restaurant.jpg", 100, -34.598940, -58.415550);
		Restaurante restauranteMockeado2 = new Restaurante(null, "El Club de la Milanesa", 5.0, "Arieta 5000",
				"restaurant2.jpg", 100, -34.598940, -58.415550);
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
	public void QueAlBuscarUnRestaurantePorNombreQueExistaMeMuestreEseRestaurante() throws RestauranteNoEncontrado, NoHayRestaurantes {
		// preparacion
		List<Restaurante> restaurantesMockeados = new ArrayList<>();
		Restaurante restauranteMockeado1 = new Restaurante(null, "La Farola", 4.0, "Santa Maria 3500",
				"restaurant.jpg", 100, -34.598940, -58.415550);
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
                5.0, "Arieta 5000", "restaurant.jpg", 100, -34.598940, -58.415550));
        restaurantesMock.add(new Restaurante(null, "La Trattoria Bella Italia",
                3.0, "Avenida Libertador 789", "restaurant2.jpg", 100, -34.598940, -58.415550));
        restaurantesMock.add(new Restaurante(null, "La Parrilla de Don Juan",
                4.0, "Avenida Central 456", "restaurant3.jpg", 100, -34.598940, -58.415550));


		when(this.servicioRestauranteMock.get()).thenReturn(restaurantesMock);

		// ejecucion
		ModelAndView mav = this.controladorHome.mostrarHome();

		// verificacion
		List<Restaurante> restaurantes = (List<Restaurante>) mav.getModel().get("restaurantes");
		assertEquals(3, restaurantes.size());
	}

}
