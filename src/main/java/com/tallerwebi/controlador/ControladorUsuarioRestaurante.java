package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Email;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayCategorias;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorUsuarioRestaurante {
    private final ServicioRestaurante servicioRestaurante;
    private final ServicioReserva servicioReserva;
    private final ServicioCategoria servicioCategoria;
    private final Email servicioEmail;
    private static final String ERROR_NAME = "error";

    public ControladorUsuarioRestaurante(ServicioRestaurante servicioRestaurante,
                                         ServicioReserva servicioReserva, ServicioCategoria servicioCategoria, Email servicioEmail) {
        this.servicioRestaurante = servicioRestaurante;
        this.servicioReserva = servicioReserva;
        this.servicioCategoria = servicioCategoria;
        this.servicioEmail = servicioEmail;
    }

    private void addUserInfoToModel(ModelMap model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ROL") != null) {
            String rolUsuario = (String) session.getAttribute("ROL");
            String emailUsuario = (String) session.getAttribute("email");
            model.put("usuarioLogueado", true);
            model.put("rolUsuario", rolUsuario);
            model.put("emailUser", emailUsuario);
        } else {
            model.put("usuarioLogueado", false);
            model.put("rolUsuario", null);
        }
    }

    @GetMapping(path = "/perfil_restaurante")
    public ModelAndView homePerfilRestaurante(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        long ganancia = 0;
        HttpSession sesion = request.getSession();
        Long userId = (Long) sesion.getAttribute("userId");
        Restaurante restaurante = null;
        try {
            restaurante = servicioRestaurante.consultarPorUsuarioId(userId);
            model.put("restaurantId", restaurante.getId());
            model.put("restauranteNombre", restaurante.getNombre());

            try{
                List<Reserva> reservas = servicioReserva.buscarReservasDelRestaurante(restaurante.getId());
                if (reservas != null && !reservas.isEmpty()) {
                    ganancia = reservas.size() * 5000;
                    model.put("reservas", reservas);
                }
            }catch(NoHayReservas e){
                model.put("error", e.getMessage());
            }
            model.put("ganancias", ganancia);
        }catch (RestauranteNoEncontrado e) {
            model.put("error", e.getMessage());
        } catch (Exception e) {
            model.put("error", "Error del servidor: " + e.getMessage());
        }

        addUserInfoToModel(model, request);
        return new ModelAndView("perfil_restaurante-home", model);
    }

    @GetMapping(path = "/perfil_restaurante/{id}/crearPromocion")
    public ModelAndView cargarFormPromocion(@PathVariable("id") Long id,
                                            HttpServletRequest request){
        ModelMap model = new ModelMap();
        try {
            Restaurante restaurante = servicioRestaurante.consultar(id);
            //List<Categoria> categorias = servicioCategoria.get();
            model.addAttribute("idRestaurante", id);
            //model.addAttribute("categorias", categorias);
            model.put("restaurantId", id);
            model.put("restauranteNombre", restaurante.getNombre());
        } catch (RestauranteNoEncontrado e) {
            model.put(ERROR_NAME, "No existe el restaurante");
        } /*catch (NoHayCategorias e) {
               // model.put(ERROR_NAME, "No hay categorías disponibles");
        }*/ catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView("crear_promocion", model);
    }

    @PostMapping(path = "/perfil_restaurante/{id}/enviarPromocion", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public ModelAndView enviarPromocion(@PathVariable("id") Long id,
                                        @RequestParam("subject") String subject,
                                        @RequestParam("text") String text,
                                        HttpServletRequest request) {

        ModelMap model = new ModelMap();
        try {
            List<String> emails = servicioReserva.obtenerEmailsUsuariosPorRestaurante(id);
            for (String email : emails) {
                servicioEmail.generarEmailPromocionPDF(email, subject, text);
            }
            model.addAttribute("message", "Promoción enviada con éxito");
            model.put("restaurantId", id);
        } catch (NoHayReservas e) {
            model.addAttribute("error", "No hay reservas para este restaurante");
        } catch (Exception e) {
            model.addAttribute("error", "Error al enviar la promoción: " + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView("promocion_enviada", model);
    }
}
