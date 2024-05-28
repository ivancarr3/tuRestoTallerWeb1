package com.tallerwebi.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.servicio.ServicioReserva;

@Controller
public class ControladorUsuario {
	
	private static final String MODEL_NAME = "reservas";
	private final ServicioReserva servicioReserva;

	@Autowired
    public ControladorUsuario(ServicioReserva servicioReserva){
        this.servicioReserva = servicioReserva;
		
	}
	
	@GetMapping(path = "/usuarioPerfil")
    public ModelAndView cargarUsuarioPerfil() {
            return new ModelAndView("usuario_perfil");
        }
	@GetMapping(path = "/misReservas")
    public ModelAndView mostrarMisReservas() {
        ModelMap model = new ModelMap();
        List<Reserva> reservas = servicioReserva.buscarTodasLasReservas();
		model.put(MODEL_NAME, reservas);
            return new ModelAndView("mis_reservas", model);
        }
    }

