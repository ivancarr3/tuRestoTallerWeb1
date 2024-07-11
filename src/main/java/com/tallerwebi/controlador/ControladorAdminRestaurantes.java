package com.tallerwebi.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.servicio.ServicioRestaurante;

import java.util.List;

@Controller
public class ControladorAdminRestaurantes {

    private final ServicioRestaurante servicioRestaurante;
    private static final String REDIRECT = "redirect:/admin-restaurantes";

    @Autowired
    public ControladorAdminRestaurantes(ServicioRestaurante servicioRestaurante) {
        this.servicioRestaurante = servicioRestaurante;
    }

    @GetMapping("/admin-restaurantess")
    public ModelAndView homeAdministradorRestaurantes() {
        List<Restaurante> restaurantesDeshabilitados = servicioRestaurante.obtenerRestaurantesDeshabilitados();
        List<Restaurante> restaurantesHabilitados = servicioRestaurante.obtenerRestaurantesHabilitados();

        ModelMap model = new ModelMap();

        model.addAttribute("restaurantesHabilitados", restaurantesHabilitados);
        model.addAttribute("restaurantesDeshabilitados", restaurantesDeshabilitados);

        return new ModelAndView("admin-restaurantes", model);
    }

    @PostMapping("/restaurantes/habilitar")
    public ModelAndView habilitarRestaurante(@RequestParam("id") Long id) {
        ModelMap model = new ModelMap();

        servicioRestaurante.habilitarRestaurante(id);

        return new ModelAndView(REDIRECT, model);
    }

    @PostMapping("/restaurantes/deshabilitar")
    public ModelAndView deshabilitarRestaurante(@RequestParam("id") Long id) {
        ModelMap model = new ModelMap();

        servicioRestaurante.deshabilitarRestaurante(id);

        return new ModelAndView(REDIRECT, model);
    }

    @PostMapping("/restaurantes/eliminar")
    public ModelAndView eliminarRestaurante(@RequestParam("id") Long id) {
        servicioRestaurante.eliminarRestaurantePorId(id);
        return new ModelAndView(REDIRECT);
    }
}
