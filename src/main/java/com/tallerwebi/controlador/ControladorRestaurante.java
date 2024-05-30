package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioCategoria;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioRestaurante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Controller
public class ControladorRestaurante {


    private final ServicioRestaurante servicioRestaurante;
    private final ServicioPlato servicioPlato;
    private static final String MODEL_NAME = "restaurantes";
    private static final String ERROR_NAME = "error";

    public ControladorRestaurante(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato){
        this.servicioRestaurante = servicioRestaurante;
        this.servicioPlato = servicioPlato;
    }

    @RequestMapping(path = "/restaurante/{id}", method = RequestMethod.GET)
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
            model.put("restaurante", restaurante);
            return new ModelAndView("restaurante", model);
        } catch (RestauranteNoEncontrado error) {
            model.put("errorId", "No se encontró el restaurante" );
            model.put(MODEL_NAME, servicioRestaurante.get());
            return new ModelAndView("home", model);
        } catch (NoHayPlatos error) {
            model.put("error", "No hay platos en este restaurante");
            return new ModelAndView("restaurante", model);
        } catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
            System.out.println(e.getMessage());
            return new ModelAndView("home", model);
        }
    }

    @RequestMapping(path = "/restaurante/{id}/filtrarPlato", method = RequestMethod.POST)
    public ModelAndView filtrarPlato(@PathVariable("id") Long id_restaurante, @RequestParam("precio") String precioStr) throws PlatoNoEncontrado {
        Double precio = Double.valueOf(precioStr);
        List<Plato> platos;
        ModelMap model = new ModelMap();
        try {
            platos = servicioPlato.consultarPlatoPorPrecio(precio);
            model.put("platos", platos);

            Restaurante restaurante = servicioRestaurante.consultar(id_restaurante);
            model.put("restaurante", restaurante);

            return new ModelAndView("restaurante", model);
        }catch (PlatoNoEncontrado e) {
            model.put("error", "No existen platos");
            return new ModelAndView("restaurante", model);
        }
        catch (Exception e) {
            model.put(ERROR_NAME, "Error del servidor" + e.getMessage());
            return new ModelAndView("restaurante", model);
        }
    }
}
