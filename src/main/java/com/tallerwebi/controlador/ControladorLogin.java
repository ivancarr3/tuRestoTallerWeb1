package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.excepcion.UsuarioNoActivado;
import com.tallerwebi.servicio.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorLogin {

    private final ServicioLogin servicioLogin;
    private static final String ERROR = "error";
    private static final String VIEW_NAME = "nuevo-usuario";
    private static final String LOGIN = "login";
    private static final String DATOS_LOGIN = "datosLogin";

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/login")
    public ModelAndView irALogin() {
        ModelMap model = new ModelMap();
        model.put(DATOS_LOGIN, new DatosLogin());
        return new ModelAndView(LOGIN, model);
    }

    @PostMapping("/validar-login")
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
            if (usuarioBuscado != null) {
                request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
                request.getSession().setAttribute("email", usuarioBuscado.getEmail());
                return new ModelAndView("redirect:/home");
            } else {
                model.put(ERROR, "Usuario o clave incorrecta");
            }
        } catch (UsuarioNoActivado e) {
            model.put(ERROR, "Esta cuenta no está activada.");
        }
        return new ModelAndView(LOGIN, model);
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();
        try {
            servicioLogin.registrar(usuario);
            model.put(DATOS_LOGIN, new DatosLogin());
            model.put("mensaje", "Registro exitoso. Por favor, revisa tu correo para activar la cuenta.");
            return new ModelAndView(LOGIN, model);
        } catch (UsuarioExistente e) {
            model.put(ERROR, "El usuario ya existe");
        } catch (Exception e) {
            model.put(ERROR, "Error al registrar el nuevo usuario: " + e.getMessage());
        }
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping("/nuevo-usuario")
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping("/confirmar")
    public ModelAndView confirmarCuenta(@RequestParam("token") String token) {
        ModelMap model = new ModelMap();
        try {
            servicioLogin.activarUsuario(token);
            model.put(DATOS_LOGIN, new DatosLogin());
            model.put("mensaje", "Cuenta activada con éxito. Por favor, inicie sesión.");
        } catch (NoExisteUsuario e) {
            model.put(ERROR, "Token inválido");
        }
        return new ModelAndView(LOGIN, model);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("redirect:/login");
    }
}
