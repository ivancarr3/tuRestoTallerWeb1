package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Controller
public class ControladorRestaurante {

    private final ServicioRestaurante servicioRestaurante;
    private final ServicioPlato servicioPlato;
    private static final String MODEL_NAME_SINGULAR = "restaurante";
    private static final String ERROR_NAME = "error";

    public ControladorRestaurante(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato){
        this.servicioRestaurante = servicioRestaurante;
        this.servicioPlato = servicioPlato;
    }

    @GetMapping(path = "/restaurante/{id}")
    public ModelAndView mostrarRestaurante(@PathVariable("id") Long id) throws RestauranteNoEncontrado, NoHayRestaurantes, NoHayPlatos {
        ModelMap model = new ModelMap();
        try {
            Restaurante restaurante = servicioRestaurante.consultar(id);
            List<Plato> platos = servicioPlato.getPlatosDeRestaurante(id);
            Map<Categoria, List<Plato>> platosPorCategoria = platos.stream().collect(Collectors.groupingBy(Plato::getCategoria));

            List<Plato> platosRecomendados = platos.stream()
                    .filter(Plato::isEsRecomendado)
                    .collect(Collectors.toList());

            model.put("platosPorCategoria", platosPorCategoria);
            model.put("platosRecomendados", platosRecomendados);
            model.put("datosReserva", new DatosReserva());
            model.put(MODEL_NAME_SINGULAR, restaurante);
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (RestauranteNoEncontrado error) {
            model.put("errorId", "No se encontró el restaurante" );
            model.put(MODEL_NAME_SINGULAR, servicioRestaurante.get());
            model.put("datosReserva", new DatosReserva());
            return new ModelAndView("home", model);
        } catch (NoHayPlatos error) {
            model.put(ERROR_NAME, "No hay platos en este restaurante");
            model.put("datosReserva", new DatosReserva());
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
            model.put("datosReserva", new DatosReserva());
            return new ModelAndView("home", model);
        }
    }

    @PostMapping(path = "/restaurante/filtrarPlato")
    public ModelAndView filtrarPlato(@RequestParam("idRestaurante") Long idRestaurante, @RequestParam("precioPlato") String precioStr) throws PlatoNoEncontrado, RestauranteNoEncontrado {
        Double precio = Double.valueOf(precioStr);
        List<Plato> platos;
        ModelMap model = new ModelMap();
        try {
            Restaurante restaurante = servicioRestaurante.consultar(idRestaurante);
            platos = servicioPlato.consultarPlatoPorPrecio(precio);

            Map<Categoria, List<Plato>> platosPorCategoria = platos.stream()
                    .collect(Collectors.groupingBy(Plato::getCategoria));

            List<Plato> platosRecomendados = platos.stream()
                    .filter(Plato::isEsRecomendado)
                    .collect(Collectors.toList());

            model.put("platosPorCategoria", platosPorCategoria);
            model.put("platosRecomendados", platosRecomendados);
            model.put(MODEL_NAME_SINGULAR, restaurante);
            model.put("datosReserva", new DatosReserva());
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (PlatoNoEncontrado e) {
            model.put(ERROR_NAME, "No existen platos");
            model.put("datosReserva", new DatosReserva());
            model.put(MODEL_NAME_SINGULAR, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (RestauranteNoEncontrado e) {
            model.put(ERROR_NAME, "No existe el restaurante");
            model.put("datosReserva", new DatosReserva());
            model.put(MODEL_NAME_SINGULAR, servicioRestaurante.consultar(idRestaurante));
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        } catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
            model.put("datosReserva", new DatosReserva());
            return new ModelAndView(MODEL_NAME_SINGULAR, model);
        }
    }
}
