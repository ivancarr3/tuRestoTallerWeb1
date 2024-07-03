package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;

import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

	private final ServicioRestaurante servicioRestaurante;
	private final ServicioPlato servicioPlato;
	private static final String MODEL_NAME = "restaurantes";
	private static final String ERROR_NAME = "error";
	private static final String VIEW_NAME = "home";

	@Autowired
	public ControladorHome(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato) {
		this.servicioRestaurante = servicioRestaurante;
		this.servicioPlato = servicioPlato;
	}

	@GetMapping(path = "/")
	public ModelAndView inicio() {
		return new ModelAndView("redirect:/home");
	}


	@PostMapping(path = "/home")
	public ModelAndView buscar(@ModelAttribute("busqueda") String busqueda)
			throws RestauranteNoEncontrado, NoHayRestaurantes {
		ModelMap model = new ModelMap();
		try {
			List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorNombre(busqueda);
			model.put(MODEL_NAME, restaurantes);
			return new ModelAndView(VIEW_NAME, model);
		} catch (RestauranteNoEncontrado error) {
			model.put("errorBusqueda", "No se encontraron restaurantes con el nombre " + busqueda);
			model.put(MODEL_NAME, servicioRestaurante.get());
			return new ModelAndView(VIEW_NAME, model);
		} catch (RuntimeException e) {
			model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
			return new ModelAndView(VIEW_NAME, model);
		}
	}

	@PostMapping(path = "/filtrar")
	public ModelAndView filtrar(@RequestParam(value = "filtrado", required = false) Double estrella,
			@RequestParam(value = "filtro_orden", required = false) String tipoDeOrden)
			throws RestauranteNoEncontrado, NoHayRestaurantes {
		ModelMap model = new ModelMap();
		try {
			List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorFiltros(estrella, tipoDeOrden);
			model.put(MODEL_NAME, restaurantes);
			return new ModelAndView(VIEW_NAME, model);
		} catch (RestauranteNoEncontrado error) {
			model.put("errorFiltro", "No se encontraron restaurantes");
			model.put(MODEL_NAME, servicioRestaurante.get());
			return new ModelAndView(VIEW_NAME, model);
		} catch (RuntimeException e) {
			model.put(ERROR_NAME, "Error del servidor " + e.getMessage());
			return new ModelAndView(VIEW_NAME, model);
		}
	}

	@PostMapping(path = "/filtrar_capacidad")
	public ModelAndView filtrar(@RequestParam(value = "capacidadPersonas") Integer capacidad) throws NoHayRestaurantes {
		ModelMap model = new ModelMap();
		try {
			List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorEspacio(capacidad);
			model.put(MODEL_NAME, restaurantes);
			return new ModelAndView(VIEW_NAME, model);
		} catch (NoHayRestaurantes error) {
			model.put("errorFiltro", "No se encontraron restaurantes");
			model.put(MODEL_NAME, servicioRestaurante.get());
			return new ModelAndView(VIEW_NAME, model);
		} catch (RuntimeException e) {
			model.put(ERROR_NAME, "Error del servidor " + e.getMessage());
			return new ModelAndView(VIEW_NAME, model);
		}
	}

	@GetMapping(path = "/home")
	public ModelAndView mostrarHome() throws NoHayRestaurantes {
		List<Restaurante> restaurantes = servicioRestaurante.get();
		ModelMap model = new ModelMap();
		model.put(MODEL_NAME, restaurantes);
		return new ModelAndView(VIEW_NAME, model);
	}
}
