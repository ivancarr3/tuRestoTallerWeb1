package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;

import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

	private final ServicioRestaurante servicioRestaurante;

	@Autowired
	public ControladorHome(ServicioRestaurante servicioRestaurante){
		this.servicioRestaurante = servicioRestaurante;
	}


	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView inicio() {
		return new ModelAndView("redirect:/home");
	}

	@RequestMapping(path = "/home", method = RequestMethod.POST)
	public ModelAndView buscar(@ModelAttribute("busqueda") String busqueda) throws RestauranteNoEncontrado {
		List<Restaurante> restaurantes;
		if (busqueda != null) {
			ModelMap model = new ModelMap();
			String modelName = "restaurantes";
			try {
				restaurantes = servicioRestaurante.consultarRestaurantePorNombre(busqueda);
				model.put(modelName, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("error", "No se encontraron restaurantes con el nombre " + busqueda);
				model.put(modelName, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put("error", "Error del servidor" + e.getMessage());
				return new ModelAndView("home");
			}
		} else {
			return new ModelAndView("home");
		}
	}


	@RequestMapping(path = "/filtrar", method = RequestMethod.POST)
	public ModelAndView filtrar(@RequestParam(value = "filtrado", required = false) Double estrella,
								@RequestParam(value = "filtro_orden", required = false) String tipoDeOrden) throws RestauranteNoEncontrado {
		List<Restaurante> restaurantes;
		ModelMap model = new ModelMap();
		String modelName = "restaurantes";
		if (estrella != null) {
			try {
				restaurantes = servicioRestaurante.consultarRestaurantePorEstrellas(estrella);
				model.put(modelName, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("errorFiltro", "No se encontraron restaurantes con " +estrella+ " estrella/s" );
				model.put(modelName, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put("error", "Error del servidor" + e.getMessage());
				return new ModelAndView("home");
			}
		} else if(tipoDeOrden != null) {
			try {
				restaurantes = servicioRestaurante.consultarOrdenPorEstrellas(tipoDeOrden);
				model.put(modelName, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("errorFiltro", "No se encontraron restaurantes" );
				model.put(modelName, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put("error", "Error del servidor" + e.getMessage());
				return new ModelAndView("home");
			}
		} else {
			return new ModelAndView("home");
		}
	}


	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public ModelAndView mostrarHome() {
		List<Restaurante> restaurantes = servicioRestaurante.get();
		ModelMap model = new ModelMap();
		model.put("restaurantes", restaurantes);
		return new ModelAndView("home", model);
	}
}
