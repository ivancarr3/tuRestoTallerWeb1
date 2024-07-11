package com.tallerwebi.controlador;


import com.tallerwebi.dominio.Email;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;

import com.tallerwebi.servicio.ServicioRestaurante;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import java.util.List;

@Controller
public class ControladorAdminRestaurantes {


    private final ServicioRestaurante servicioRestaurante;
    private final ServicioReserva servicioReserva;
    private final Email servicioEmail;
    private static final String REDIRECT = "redirect:/admin-restaurantes";


    @Autowired
    public ControladorAdminRestaurantes(ServicioRestaurante servicioRestaurante, ServicioReserva servicioReserva, Email servicioEmail) {
        this.servicioRestaurante = servicioRestaurante;
        this.servicioReserva = servicioReserva;
        this.servicioEmail = servicioEmail;
    }


    private void addUserInfoToModel(ModelMap model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ROL") != null) {
            String rolUsuario = (String) session.getAttribute("ROL");
            model.put("usuarioLogueado", true);
            model.put("rolUsuario", rolUsuario);
        } else {
            model.put("usuarioLogueado", false);
            model.put("rolUsuario", null);
        }
    }


    @GetMapping("/admin-restaurantes")
    public ModelAndView homeAdministradorRestaurantes(HttpServletRequest request) {
        List<Restaurante> restaurantesDeshabilitados = servicioRestaurante.obtenerRestaurantesDeshabilitados();
        List<Restaurante> restaurantesHabilitados = servicioRestaurante.obtenerRestaurantesHabilitados();

        ModelMap model = new ModelMap();

        model.addAttribute("restaurantesHabilitados", restaurantesHabilitados);
        model.addAttribute("restaurantesDeshabilitados", restaurantesDeshabilitados);
        addUserInfoToModel(model, request);
        return new ModelAndView("admin-restaurantes-home", model);
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

