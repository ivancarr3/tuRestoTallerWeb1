package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorUsuario {

    private static final String MODEL_NAME = "reservas";
    private static final String ERROR = "error";
    private final ServicioReserva servicioReserva;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioReserva servicioReserva, ServicioUsuario servicioUsuario) {
        this.servicioReserva = servicioReserva;
        this.servicioUsuario = servicioUsuario;
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

    @GetMapping(path = "/usuarioPerfil")
    public ModelAndView cargarUsuarioPerfil(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            HttpSession session = request.getSession(false);
            String email = (String) session.getAttribute("email");
            Usuario usuario = servicioUsuario.buscar(email);
            List<Reserva> reservas = servicioReserva.buscarReservasDelUsuario(usuario.getId());
            model.put(MODEL_NAME, reservas);
            model.put("username", usuario.getEmail());
        } catch (NoHayReservas e) {
            model.put(ERROR, "Todavía no has hecho ninguna reserva.");
        } catch (NoExisteUsuario e) {
            model.put(ERROR, "Usuario no encontrado.");
        } catch (Exception e) {
            model.put(ERROR, "Error del servidor: " + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView("usuario_perfil", model);
    }
}
