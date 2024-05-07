package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioHome;

import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorHome {

	private ServicioHome servicioHome;
	private ServicioRestaurante servicioRestaurante;
	
	@Autowired
    public ControladorHome(ServicioHome servicioHome, ServicioRestaurante servicioRestaurante){
        this.servicioHome = servicioHome;
        this.servicioRestaurante = servicioRestaurante;
    }

	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public ModelAndView irAHome(@RequestParam(required = false) String busqueda) throws RestauranteNoEncontrado {
		if (busqueda != null) {
			ModelMap model = new ModelMap();
			List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorNombre(busqueda);
			System.out.println("Total de restaurantes que encontre: " + restaurantes.size());
			model.put("restaurantes",restaurantes);
			return new ModelAndView("home",model);
		} else {
	        return new ModelAndView("home");
		}
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView inicio() {
		return new ModelAndView("redirect:/home");
	}
}
