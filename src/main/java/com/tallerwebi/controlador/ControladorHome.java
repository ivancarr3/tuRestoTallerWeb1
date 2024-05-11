package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
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

	@Autowired
    public ControladorHome(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato){
        this.servicioRestaurante = servicioRestaurante;
		this.servicioPlato = servicioPlato;
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
			try {
				restaurantes = servicioRestaurante.consultarRestaurantePorNombre(busqueda);
				model.put(MODEL_NAME, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("errorBusqueda", "No se encontraron restaurantes con el nombre " + busqueda);
				model.put(MODEL_NAME, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
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
		if (estrella != null) {
			try {
				restaurantes = servicioRestaurante.consultarRestaurantePorEstrellas(estrella);
				model.put(MODEL_NAME, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("errorFiltro", "No se encontraron restaurantes con " +estrella+ " estrella/s" );
				model.put(MODEL_NAME, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
				return new ModelAndView("home");
			}
		} else if(tipoDeOrden != null) {
			try {
				restaurantes = servicioRestaurante.consultarOrdenPorEstrellas(tipoDeOrden);
				model.put(MODEL_NAME, restaurantes);
				return new ModelAndView("home", model);
			} catch (RestauranteNoEncontrado error) {
				model.put("errorFiltro", "No se encontraron restaurantes" );
				model.put(MODEL_NAME, servicioRestaurante.get());
				return new ModelAndView("home", model);
			} catch (Exception e) {
				model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
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
		model.put(MODEL_NAME, restaurantes);
		return new ModelAndView("home", model);
	}

	@RequestMapping(path = "/reserva/{id}", method = RequestMethod.GET)
	public ModelAndView reservar(@PathVariable("id") Long id) throws RestauranteNoEncontrado {
		ModelMap model = new ModelMap();
		try {
			Restaurante restaurante = servicioRestaurante.consultar(id);
			model.put("restaurante", restaurante);
			return new ModelAndView("reserva", model);
		} catch (RestauranteNoEncontrado error) {
			model.put("errorId", "No se encontró el restaurante" );
			model.put(MODEL_NAME, servicioRestaurante.get());
			return new ModelAndView("home", model);
		} catch (Exception e) {
			model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
			return new ModelAndView("home");
		}
	}

	@RequestMapping(path = "/reserva/{id}/filtrarPlato", method = RequestMethod.POST)
	public ModelAndView filtrarPlato(@PathVariable("id") Long id_restaurante, @RequestParam("precio") String precioStr) throws PlatoNoEncontrado {
		Integer precio = Integer.valueOf(precioStr);
		List<Plato> platos;
		ModelMap model = new ModelMap();
		try {
			platos = servicioPlato.consultarPlatoPorPrecio(precio);
			model.put("platos", platos);

			Restaurante restaurante = servicioRestaurante.consultar(id_restaurante);
			model.put("restaurante", restaurante);

			return new ModelAndView("reserva", model);
		}catch (PlatoNoEncontrado e) {
			model.put("error", "No existen platos");
			return new ModelAndView("reserva", model);
		}
		catch (Exception e) {
			model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
			return new ModelAndView("reserva", model);
		}
	}

}
