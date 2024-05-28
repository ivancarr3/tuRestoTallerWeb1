package com.tallerwebi.controlador;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

@Controller
public class ControladorReserva {

    private final ServicioRestaurante servicioRestaurante;
    private static final String MODEL_NAME = "restaurante";
    private static final String ERROR_NAME = "error";
    private final ServicioReserva servicioReserva;

    public ControladorReserva(ServicioRestaurante servicioRestaurante, ServicioReserva servicioReserva) {
        this.servicioRestaurante = servicioRestaurante;
        this.servicioReserva = servicioReserva;
    }

    @PostMapping(path = "/reservar")
    public ModelAndView reservar(@RequestParam("id_form") Long idRestaurante,
                                 @RequestParam("nombre_form") String nombreForm,
                                 @RequestParam("email_form") String emailForm,
                                 @RequestParam("num_form") Integer numForm,
                                 @RequestParam("dni_form") Integer dniForm,
                                 @RequestParam("cant_personas_form") Integer cantPersonas,
                                 @RequestParam("fecha_form") 
								 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaForm) throws RestauranteNoEncontrado, NoHayRestaurantes, EspacioNoDisponible, DatosInvalidosReserva {
		ModelMap model = new ModelMap();

        try {
            Restaurante restauranteEncontrado = servicioRestaurante.consultar(idRestaurante);
            servicioReserva.crearReserva(restauranteEncontrado, nombreForm, emailForm,
                    numForm, dniForm, cantPersonas, fechaForm);
            return new ModelAndView("reserva_exitosa", model);
        } catch (RestauranteNoEncontrado error) {
            model.put("errorId", "No se encontró el restaurante");
            model.put(MODEL_NAME, servicioRestaurante.consultar(1L));
            return new ModelAndView(MODEL_NAME, model);
        } catch (DatosInvalidosReserva | EspacioNoDisponible error) {
            model.put("errorForm", error.getMessage());
            model.put(MODEL_NAME, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME, model);
        } catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
            model.put(MODEL_NAME, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME, model);
        }
    }

    @GetMapping(path = "/reservar")
    public ModelAndView getRequest() {
        return new ModelAndView("redirect:/home");
    }
}
