package com.tallerwebi.controlador;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;

@Controller
public class ControladorHome {

    private final ServicioRestaurante servicioRestaurante;
    private final ServicioPlato servicioPlato;
    private final ServicioGeocoding servicioGeocoding;
    private final ServicioUsuario servicioUsuario;
    private final ServicioCategoria servicioCategoria;
    private static final String MODEL_NAME = "restaurantes";
    private static final String ERROR_NAME = "error";
    private static final String ERROR_FILTRO = "errorFiltro";
    private static final String ERROR_SERVIDOR = "Error del servidor: ";
    private static final String VIEW_NAME = "home";

    @Autowired
    public ControladorHome(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato,
                           ServicioGeocoding servicioGeocoding, ServicioUsuario servicioUsuario, ServicioCategoria servicioCategoria) {
        this.servicioRestaurante = servicioRestaurante;
        this.servicioPlato = servicioPlato;
        this.servicioGeocoding = servicioGeocoding;
        this.servicioUsuario = servicioUsuario;
        this.servicioCategoria = servicioCategoria;
    }

    private void addUserInfoToModel(ModelMap model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ROL") != null) {
            String rolUsuario = (String) session.getAttribute("ROL");
            List<Categoria> categoriasPref = (List<Categoria>) session.getAttribute("categoriasPref");
            model.put("usuarioLogueado", true);
            model.put("rolUsuario", rolUsuario);
            model.put("categoriasPref", categoriasPref);

            List<Categoria> categoriasDisponibles = servicioUsuario.obtenerCategoriasDisponibles(categoriasPref);
            model.put("categoriasDisponibles", categoriasDisponibles);

        } else {
            model.put("usuarioLogueado", false);
            model.put("rolUsuario", null);
        }

    }

    @GetMapping(path = "/")
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/home");
    }

    @PostMapping(path = "/home")
    public ModelAndView buscar(@ModelAttribute("busqueda") String busqueda, HttpServletRequest request)
            throws NoHayRestaurantes {

        ModelMap model = new ModelMap();
        try {
            List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorNombre(busqueda);
            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            for (Restaurante restaurante : restaurantes) {
                Double distancia = 0.1 + (5.0 - 0.1) * rand.nextDouble();
                distancia = Double.parseDouble(df.format(distancia));
                restaurante.setDistancia(distancia);
            }
            model.put(MODEL_NAME, restaurantes);
        } catch (RestauranteNoEncontrado e) {
            model.put("errorBusqueda", "No se encontraron restaurantes con el nombre " + busqueda);
            model.put(MODEL_NAME, servicioRestaurante.obtenerRestaurantesHabilitados());

        } catch (Exception e) {
            model.put(ERROR_NAME, ERROR_SERVIDOR + e.getMessage());

        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @PostMapping(path = "/filtrar")
    public ModelAndView filtrar(@RequestParam(value = "filtrado", required = false) Double estrella,
                                @RequestParam(value = "filtro_orden", required = false) String tipoDeOrden, HttpServletRequest request)
            throws NoHayRestaurantes {

        ModelMap model = new ModelMap();
        try {
            List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorFiltros(estrella, tipoDeOrden);
            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            for (Restaurante restaurante : restaurantes) {
                Double distancia = 0.1 + (5.0 - 0.1) * rand.nextDouble();
                distancia = Double.parseDouble(df.format(distancia));
                restaurante.setDistancia(distancia);
            }
            model.put(MODEL_NAME, restaurantes);

        } catch (RestauranteNoEncontrado e) {
            model.put(ERROR_FILTRO, "No se encontraron restaurantes");
            model.put(MODEL_NAME, servicioRestaurante.obtenerRestaurantesHabilitados());
        } catch (Exception e) {
            model.put(ERROR_NAME, ERROR_SERVIDOR + e.getMessage());

        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @PostMapping(path = "/filtrar_capacidad")
    public ModelAndView filtrarPorCapacidad(@RequestParam("capacidadPersonas") Integer capacidad,
                                            HttpServletRequest request) throws NoHayRestaurantes {
        ModelMap model = new ModelMap();
        try {
            List<Restaurante> restaurantes = servicioRestaurante.consultarRestaurantePorEspacio(capacidad);
            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            for (Restaurante restaurante : restaurantes) {
                Double distancia = 0.1 + (5.0 - 0.1) * rand.nextDouble();
                distancia = Double.parseDouble(df.format(distancia));
                restaurante.setDistancia(distancia);
            }
            model.put(MODEL_NAME, restaurantes);

        } catch (NoHayRestaurantes e) {
            model.put(ERROR_FILTRO, "No se encontraron restaurantes");
            model.put(MODEL_NAME, servicioRestaurante.obtenerRestaurantesHabilitados());
        } catch (Exception e) {
            model.put(ERROR_NAME, ERROR_SERVIDOR + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping(path = "/buscar_direccion")
    public ModelAndView buscarPorDireccion(@RequestParam("direccion") String direccion,
                                           @RequestParam(value = "distanciaMaxima", required = false, defaultValue = "5.0") Double distanciaMaxima,
                                           HttpServletRequest request) throws NoHayRestaurantes {
        ModelMap model = new ModelMap();
        try {
            List<Restaurante> restaurantes = servicioRestaurante.filtrarPorDireccion(direccion, distanciaMaxima);
            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            for (Restaurante restaurante : restaurantes) {
                Double distancia = 0.1 + (5.0 - 0.1) * rand.nextDouble();
                distancia = Double.parseDouble(df.format(distancia));
                restaurante.setDistancia(distancia);
            }
            model.put(MODEL_NAME, restaurantes);
        } catch (NoHayRestaurantes e) {
            model.put(ERROR_FILTRO, e.getMessage() + " en esa dirección");
            model.put(MODEL_NAME, servicioRestaurante.obtenerRestaurantesHabilitados());
        } catch (NoExisteDireccion e) {
            model.put(ERROR_FILTRO, e.getMessage());
            model.put(MODEL_NAME, servicioRestaurante.obtenerRestaurantesHabilitados());
        } catch (Exception e) {
            model.put(ERROR_NAME, ERROR_SERVIDOR + e.getMessage());

        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping(path = "/home")
    public ModelAndView mostrarHome(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            List<Restaurante> restaurantes = servicioRestaurante.obtenerRestaurantesHabilitados();
            if (restaurantes == null || restaurantes.isEmpty()) {
                throw new NoHayRestaurantes();
            }

            Random rand = new Random();
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

            for (Restaurante restaurante : restaurantes) {
                Double distancia = 0.1 + (5.0 - 0.1) * rand.nextDouble();
                distancia = Double.parseDouble(df.format(distancia));
                restaurante.setDistancia(distancia);
            }

            model.put(MODEL_NAME, restaurantes);

            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("email") != null) {
                String email = (String) session.getAttribute("email");
                Usuario usuario = servicioUsuario.buscarUsuarioPorEmail(email);
                List<Categoria> categoriasPref = usuario.getCategorias();
                List<Categoria> todasCategorias = servicioCategoria.get();
                todasCategorias.removeAll(categoriasPref);
                model.put("categoriasNoPreferidas", todasCategorias);
            }
        } catch (NoHayRestaurantes e) {
            model.put(ERROR_NAME, "No hay restaurantes disponibles.");
        } catch (NoExisteUsuario e) {
            throw new RuntimeException(e);
        } catch (NoHayCategorias e) {
            throw new RuntimeException(e);
        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @GetMapping(path = "/home/platos/{categoria}")
    public ModelAndView filtrarPlatosCategoria(@PathVariable String categoria, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            List<Plato> platos = servicioPlato.getPlatosPorCategoria(categoria);
            model.put("platos", platos);
        } catch (NoHayPlatos e) {
            model.put(ERROR_NAME, "No hay platos disponibles para esa categoría.");
        }
        addUserInfoToModel(model, request);
        return new ModelAndView(VIEW_NAME, model);
    }

    @PostMapping(path = "/agregar-categoria")
    public ModelAndView agregarCategoriaPref(@RequestParam("categoriaId") Long categoriaId, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("email") != null) {
                String email = (String) session.getAttribute("email");
                servicioUsuario.agregarCategoriaPrefUser(email, categoriaId);

                Usuario usuario = servicioUsuario.buscarUsuarioPorEmail(email);
                session.setAttribute("categoriasPref", usuario.getCategorias());
            }
        } catch (Exception e) {
            model.put(ERROR_NAME, ERROR_SERVIDOR + e.getMessage());
        }
        addUserInfoToModel(model, request);
        return new ModelAndView("redirect:/home", model);
    }
}
