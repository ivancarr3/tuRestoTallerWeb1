package com.tallerwebi.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorInforestaurante {

    @RequestMapping(path = "/inforestaurante", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("inforestaurante");
    }

}
