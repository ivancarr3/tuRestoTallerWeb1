package com.tallerwebi.controlador;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.AdministradorDeRestaurante;
import com.tallerwebi.servicio.ServicioLoginAdministradorDeRestaurante;

@Controller
public class ControladorLoginAdministradorRestaurante {

    private ServicioLoginAdministradorDeRestaurante servicioLoginAdministradorDeRestaurante;

    @Autowired
    public ControladorLoginAdministradorRestaurante(
            ServicioLoginAdministradorDeRestaurante servicioLoginAdministradorDeRestaurante) {
        this.servicioLoginAdministradorDeRestaurante = servicioLoginAdministradorDeRestaurante;
    }

    @GetMapping(path = "/admin_restaurante_login")
    public ModelAndView irAlLoginAdministrador() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("admin_restaurante_login", modelo);
    }

    @PostMapping(path = "validar-login-administrador-restaurante")
    public ModelAndView validarLoginAdministrador(@ModelAttribute("datosLogin") DatosLogin datosLogin,
            HttpServletRequest request) {

        String email = datosLogin.getEmail();
        String pass = datosLogin.getPassword();

        AdministradorDeRestaurante administradorDeRestaurante = servicioLoginAdministradorDeRestaurante
                .buscarAdministradorDeRestaurante(email,
                        pass);

        if (administradorDeRestaurante != null) {
            request.getSession().setAttribute("administradorDeRestauranteId", administradorDeRestaurante.getId());

            return new ModelAndView("redirect:/mi-resto");
        } else {
            ModelMap model = new ModelMap();
            model.addAttribute("error", "usuario no encontrado");
            return new ModelAndView("admin_restaurante_login", model);
        }

    }

}
