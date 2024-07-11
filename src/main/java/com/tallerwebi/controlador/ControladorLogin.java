package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorLogin {

    private final ServicioLogin servicioLogin;
    private final ServicioCategoria servicioCategoria;
    private static final String ERROR = "error";
    private static final String VIEW_NAME = "nuevo-usuario";
    private static final String LOGIN = "login";
    private static final String DATOS_LOGIN = "datosLogin";

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioCategoria servicioCategoria) {
        this.servicioLogin = servicioLogin;
        this.servicioCategoria = servicioCategoria;
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
                if (!usuarioBuscado.isActivo()) {
                    throw new UsuarioNoActivado();
                }
                HttpSession session = request.getSession();
                session.setAttribute("ROL", usuarioBuscado.getRol());
                session.setAttribute("email", usuarioBuscado.getEmail());
                session.setAttribute("userId", usuarioBuscado.getId());

                List<Categoria> categoriasPref = new ArrayList<>(usuarioBuscado.getCategorias());
                session.setAttribute("categoriasPref", categoriasPref);

                if ("ADMIN".equals(usuarioBuscado.getRol())) {
                    return new ModelAndView("redirect:/admin-restaurantes");
                } else if("RESTO".equals(usuarioBuscado.getRol())){
                    return new ModelAndView("redirect:/perfil_restaurante");
                }else {
                    return new ModelAndView("redirect:/home");
                }
            } else {
                model.put(ERROR, "Usuario o clave incorrecta");
            }
        } catch (UsuarioNoActivado e) {
            model.put(ERROR, "Esta cuenta no está activada.");
        }
        return new ModelAndView(LOGIN, model);
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("usuario") Usuario usuario, @RequestParam(required = false) List<Long> categoriaIds) throws NoHayCategorias {
        ModelMap model = new ModelMap();

        try {
            if (categoriaIds == null) {
                categoriaIds = new ArrayList<>();
            }
            List<Categoria> categorias = servicioCategoria.getCategoriasPorIds(categoriaIds);
            usuario.setCategorias(categorias);
            servicioLogin.registrar(usuario);
            model.put(DATOS_LOGIN, new DatosLogin());
            model.put("mensaje", "Registro exitoso. Por favor, revisa tu correo para activar la cuenta.");
            return new ModelAndView(LOGIN, model);
        } catch (UsuarioExistente e) {
            List<Categoria> categorias = servicioCategoria.get();
            model.put("categorias", categorias);
            model.put(ERROR, "El usuario ya existe");
        } catch (DemasiadasPreferenciasUsuarioRegistro e){
            List<Categoria> categorias = servicioCategoria.get();
            model.put("categorias", categorias);
            model.put(ERROR, e.getMessage());
        } catch (Exception e) {
            List<Categoria> categorias = servicioCategoria.get();
            model.put("categorias", categorias);
            model.put(ERROR, "Error al registrar el nuevo usuario.");
        }
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping("/nuevo-usuario")
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        try{
            List<Categoria> categorias = servicioCategoria.get();
            model.put("categorias", categorias);
        }catch (NoHayCategorias e){
            model.put(ERROR, e.getMessage());
        }
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
