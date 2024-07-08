package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.ReservaNoEncontrada;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        Usuario usuario = null;
        Integer cantidadReservas = 0;
        try {
            usuario = servicioUsuario.buscar(email);
            if (usuario == null) {
                throw new NoExisteUsuario();
            }
            cantidadReservas = cantidadReservas = servicioReserva.buscarReservasDelUsuarioPasadas(usuario.getId()).size();
            if (cantidadReservas == 0) {
                throw new NoHayReservas();
            }
            List<Reserva> reservas = servicioReserva.buscarReservasDelUsuario(usuario.getId());
            model.put(MODEL_NAME, reservas);
        } catch (NoHayReservas e) {
            model.put(ERROR, "No tienes próximas reservas.");
        } catch (NoExisteUsuario e) {
            model.put(ERROR, "Usuario no encontrado.");
        } catch (Exception e) {
            model.put(ERROR, "Error del servidor: " + e.getMessage());
        }
        if (usuario != null) {
            model.put("username", usuario.getNombre());
        }
        model.put("cantidadReservas", cantidadReservas);
        addUserInfoToModel(model, request);
        return new ModelAndView("usuario_perfil", model);
    }

    @GetMapping(path = "/historial")
    public ModelAndView historialReservas(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            HttpSession session = request.getSession(false);
            String email = (String) session.getAttribute("email");
            Usuario usuario = servicioUsuario.buscar(email);
            List<Reserva> reservasPasadas = servicioReserva.buscarReservasDelUsuarioPasadas(usuario.getId());
            model.put(MODEL_NAME, reservasPasadas);
            model.put("user", usuario);
        } catch (NoHayReservas e) {
            model.put(ERROR, "Todavía no has asistido a ninguna reserva.");
        } catch (NoExisteUsuario e) {
            model.put(ERROR, "Usuario no encontrado.");
        } catch (Exception e) {
            model.put(ERROR, "Error del servidor: " + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView("historial_reservas_usuario", model);
    }

    @PostMapping(path = "/cancelar_reserva")
    public ModelAndView cancelarReserva(HttpServletRequest request, @RequestParam(value = "id_reserva", required = true) Long id_reserva) {
        ModelMap model = new ModelMap();
        try {
            Reserva reservaAEliminar = servicioReserva.buscarReserva(id_reserva);
            servicioReserva.cancelarReserva(reservaAEliminar);
            return new ModelAndView("redirect:/home");
        } catch (ReservaNoEncontrada e) {
            model.put(ERROR, "No tienes próximas reservas.");
            addUserInfoToModel(model, request);
            return new ModelAndView("usuario_perfil", model);
        } catch (Exception e) {
            model.put(ERROR, "Error del servidor: " + e.getMessage());
            addUserInfoToModel(model, request);
            return new ModelAndView("usuario_perfil", model);
        }
    }
}
