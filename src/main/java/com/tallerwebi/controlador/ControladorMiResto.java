package com.tallerwebi.controlador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.AdministradorDeRestaurante;
import com.tallerwebi.dominio.Plato;
import com.tallerwebi.servicio.ServicioAdministradorRestaurante;
import com.tallerwebi.servicio.ServicioLogin;
import com.tallerwebi.servicio.ServicioPlato;

@Controller
public class ControladorMiResto {
    private final ServicioPlato servicioPlato;
    private final ServicioAdministradorRestaurante servicioAdministradorRestaurante;

    @Autowired
    public ControladorMiResto(ServicioPlato servicioPlato,
            ServicioAdministradorRestaurante servicioAdministradorRestaurante) {
        this.servicioPlato = servicioPlato;
        this.servicioAdministradorRestaurante = servicioAdministradorRestaurante;
    };

    @GetMapping(path = "/mi-resto")
    public ModelAndView miResto(HttpServletRequest request) {

        Long administradorDeRestauranteId = (Long) request.getSession()
                .getAttribute("administradorDeRestauranteId");

        if (administradorDeRestauranteId == null) {
            return new ModelAndView("redirect:/admin_restaurante_login");
        }

        AdministradorDeRestaurante administradorDeRestaurante = servicioAdministradorRestaurante
                .obtenerAdministradorPorId(administradorDeRestauranteId);

        ModelMap model = new ModelMap();

        List<Plato> listaDePlatos = administradorDeRestaurante.getRestaurante().getPlatos();

        model.addAttribute("platos", listaDePlatos);

        return new ModelAndView("mi-resto", model);
    }

    @PostMapping(path = "/mi-resto/eliminarPlato")
    public ModelAndView eliminarPlato(@RequestParam("id") Long id) {
        Plato plato = new Plato();
        plato.setId(id);

        ModelMap model = new ModelMap();

        try {
            servicioPlato.eliminarPlato(plato);

        } catch (Exception e) {
            model.addAttribute("error", e.getStackTrace());
            return new ModelAndView("mi-resto", model);
        }

        return new ModelAndView("redirect:/mi-resto");
    }

}
