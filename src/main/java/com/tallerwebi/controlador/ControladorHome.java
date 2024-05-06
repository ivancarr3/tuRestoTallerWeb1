package com.tallerwebi.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.servicio.ServicioHome;
import com.tallerwebi.servicio.ServicioHome;

@Controller
public class ControladorHome {
	
	
	private ServicioHome servicioHome;
	
	@Autowired
    public ControladorHome(ServicioHome servicioHome){
        this.servicioHome = servicioHome;
    }
	

     
	 @RequestMapping(path = "/home", method = RequestMethod.GET)
	    public ModelAndView irAHome() {
	        return new ModelAndView("home");
	    }

	    @RequestMapping(path = "/", method = RequestMethod.GET)
	    public ModelAndView inicio() {
	        return new ModelAndView("redirect:/home");
	    }
}
