package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorRestaurante {

    private final ServicioRestaurante servicioRestaurante;
    private final ServicioPlato servicioPlato;
    private static final String MODEL_NAME_SINGULAR = "restaurante";
    private static final String ERROR_NAME = "error";

    public ControladorRestaurante(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato){
        this.servicioRestaurante = servicioRestaurante;
        this.servicioPlato = servicioPlato;
    }

    @GetMapping(path = "/restaurante/{id}")
    public ModelAndView mostrarRestaurante(@PathVariable("id") Long id) throws RestauranteNoEncontrado {
        ModelMap model = new ModelMap();
        try {
            Restaurante restaurante = servicioRestaurante.consultar(id);
            model.put(MODEL_NAME_SINGULAR, restaurante);
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (Exception error) {
            return new ModelAndView("redirect:/home");
        }
    }

    @PostMapping(path = "/restaurante/filtrarPlato")
    public ModelAndView filtrarPlato(@RequestParam("idRestaurante") Long idRestaurante, @RequestParam("precioPlato") String precioStr) throws PlatoNoEncontrado, RestauranteNoEncontrado {
        Double precio = Double.valueOf(precioStr);
        List<Plato> platos;
        ModelMap model = new ModelMap();
        try {
            platos = servicioPlato.consultarPlatoPorPrecio(precio);
            model.put("platos", platos);

            Restaurante restaurante = servicioRestaurante.consultar(idRestaurante);
            model.put(MODEL_NAME_SINGULAR, restaurante);

            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (PlatoNoEncontrado e) {
            model.put(ERROR_NAME, "No existen platos");
            model.put(MODEL_NAME_SINGULAR, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (RestauranteNoEncontrado e) {
            model.put(ERROR_NAME, "No existe el restaurante");
            model.put(MODEL_NAME_SINGULAR, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        }
    }
}
