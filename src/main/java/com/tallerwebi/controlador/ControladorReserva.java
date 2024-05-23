package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.ServicioReservaImpl;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.hibernate.annotations.common.reflection.XMethod;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Date;

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

    @RequestMapping(value = "/reservar", method = RequestMethod.POST)
    public ModelAndView reservar(@RequestParam(value = "id") Long id_restaurante,
                                 @RequestParam(value = "nombre_form") String nombre_form,
                                 @RequestParam(value = "email_form") String email_form,
                                 @RequestParam(value = "num_form") Integer num_form,
                                 @RequestParam(value = "dni_form") Integer dni_form,
                                 @RequestParam(value = "cant_personas_form") Integer cant_personas,
                                 @RequestParam(value = "fecha_form")
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha_form) throws RestauranteNoEncontrado, NoHayRestaurantes,
            EspacioNoDisponible, DatosInvalidosReserva {
        ModelMap model = new ModelMap();

        try {
            Restaurante restauranteEncontrado = servicioRestaurante.consultar(id_restaurante);
            servicioReserva.crearReserva(restauranteEncontrado, nombre_form, email_form,
                    num_form, dni_form, cant_personas, fecha_form);
            return new ModelAndView("reserva_exitosa", model);
        } catch (RestauranteNoEncontrado error) {
            model.put("errorId", "No se encontró el restaurante");
            model.put(MODEL_NAME, servicioRestaurante.get());
            return new ModelAndView("home", model);
        } catch (DatosInvalidosReserva error) {
            model.put("errorForm", error.getMessage());
            model.put("restaurante", servicioRestaurante.consultar(id_restaurante));
            return new ModelAndView("restaurante", model);
        } catch (EspacioNoDisponible ex) {
            model.put("errorForm", ex.getMessage());
            model.put("restaurante", servicioRestaurante.consultar(id_restaurante));
            return new ModelAndView("restaurante", model);
        }catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
            return new ModelAndView("home", model);
        }
    }

    @RequestMapping(value = "/reservar", method = RequestMethod.GET)
    public String getRequest() {
        return "redirect:/home";
    }
}
