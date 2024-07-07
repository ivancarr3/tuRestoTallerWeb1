package com.tallerwebi.controlador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.AdministradorDeRestaurante;
import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.PlatoExistente;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioAdministradorRestaurante;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioLogin;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;

@Controller
public class ControladorMiResto {
    private final ServicioPlato servicioPlato;
    private final ServicioAdministradorRestaurante servicioAdministradorRestaurante;
    private final ServicioCategoria servicioCategoria;
    private final ServicioRestaurante servicioRestaurante;

    @Autowired
    public ControladorMiResto(ServicioPlato servicioPlato,
            ServicioAdministradorRestaurante servicioAdministradorRestaurante, ServicioCategoria servicioCategoria,
            ServicioRestaurante servicioRestaurante) {
        this.servicioPlato = servicioPlato;
        this.servicioAdministradorRestaurante = servicioAdministradorRestaurante;
        this.servicioCategoria = servicioCategoria;
        this.servicioRestaurante = servicioRestaurante;
    };

    @GetMapping(path = "/mi-resto")
    public ModelAndView miResto(HttpServletRequest request) {

        Long administradorDeRestauranteId = (Long) request.getSession()
                .getAttribute("administradorDeRestauranteId");

        if (administradorDeRestauranteId == null) {
            return new ModelAndView("redirect:/admin_restaurante_login");
        }

        AdministradorDeRestaurante administradorDeRestaurante = servicioAdministradorRestaurante
                .obtenerAdministradorPorId(administradorDeRestauranteId);

        ModelMap model = new ModelMap();

        List<Plato> listaDePlatos = administradorDeRestaurante.getRestaurante().getPlatos();
        model.addAttribute("platos", listaDePlatos);

        List<Categoria> listaDCategorias = servicioCategoria.obtenerTodasLasCategorias();
        model.addAttribute("categorias", listaDCategorias);

        model.addAttribute("idResto", administradorDeRestaurante.getRestaurante().getId());

        return new ModelAndView("mi-resto", model);
    }

    @PostMapping(path = "/mi-resto/eliminarPlato")
    public ModelAndView eliminarPlato(@RequestParam("id") Long id) {
        Plato plato = new Plato();
        plato.setId(id);

        ModelMap model = new ModelMap();

        try {
            servicioPlato.eliminarPlato(plato);

        } catch (Exception e) {
            model.addAttribute("error", e.getStackTrace());
            return new ModelAndView("mi-resto", model);
        }

        return new ModelAndView("redirect:/mi-resto");
    }

    @PostMapping(path = "/mi-resto/agregarPlato")
    public ModelAndView agregarPlato(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Double precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestParam("idResto") Long idResto,
            @RequestParam("esRecomendado") String esRecomendado) {

        ModelMap modelo = new ModelMap();

        Restaurante restaurante;
        try {
            restaurante = servicioRestaurante.consultar(idResto);
        } catch (RestauranteNoEncontrado e) {
            modelo.addAttribute("error ", e.getStackTrace());
            return new ModelAndView("redirect:/mi-resto", modelo);

        }
        Categoria categoria = servicioCategoria.getCategoriaDePlato(categoriaId);
        boolean recomendado = esRecomendado == "si" ? true : false;

        Plato plato = new Plato(nombre, precio, descripcion, "ensalada.jpg", restaurante, categoria, recomendado);

        try {
            servicioPlato.crearPlato(plato);
        } catch (PlatoExistente e) {
            modelo.addAttribute("error ", e.getStackTrace());
            return new ModelAndView("redirect:/mi-resto", modelo);
        }

        return new ModelAndView("redirect:/mi-resto");
    }

    

}
