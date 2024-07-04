package com.tallerwebi.controlador;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.excepcion.NoHayPlatos;
import com.tallerwebi.dominio.excepcion.NoHayReservas;
import com.tallerwebi.dominio.excepcion.NoHayRestaurantes;
import com.tallerwebi.dominio.excepcion.PlatoNoEncontrado;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioPlato;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;

@Controller
public class ControladorRestaurante {

	private final ServicioRestaurante servicioRestaurante;
	private final ServicioPlato servicioPlato;
	private final ServicioReserva servicioReserva;
	private static final String MODEL_NAME_SINGULAR = "restaurante";
	private static final String ERROR_NAME = "error";
	private static final String DATOS_RESERVA = "datosReserva";
	private static final String MODEL_NAME_RESERVAS = "reservas";
	private static final String MODEL_NAME_GANANCIAS = "ganancias";

	public ControladorRestaurante(ServicioRestaurante servicioRestaurante, ServicioPlato servicioPlato,
			ServicioReserva servicioReserva) {
		this.servicioRestaurante = servicioRestaurante;
		this.servicioPlato = servicioPlato;
		this.servicioReserva = servicioReserva;
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

	@GetMapping(path = "/restaurante/{id}")
	public ModelAndView mostrarRestaurante(@PathVariable("id") Long id, HttpServletRequest request)
			throws NoHayRestaurantes {
		ModelMap model = new ModelMap();
		try {
			Restaurante restaurante = servicioRestaurante.consultar(id);
			List<Plato> platos = servicioPlato.getPlatosDeRestaurante(id);
			Map<Categoria, List<Plato>> platosPorCategoria = platos.stream()
					.collect(Collectors.groupingBy(Plato::getCategoria));
			List<Plato> platosRecomendados = platos.stream().filter(Plato::isEsRecomendado)
					.collect(Collectors.toList());

			model.put("platosPorCategoria", platosPorCategoria);
			model.put("platosRecomendados", platosRecomendados);
			model.put(DATOS_RESERVA, new DatosReserva());
			model.put(MODEL_NAME_SINGULAR, restaurante);
			addUserInfoToModel(model, request);
			return new ModelAndView(MODEL_NAME_SINGULAR, model);
		} catch (RestauranteNoEncontrado e) {
			model.put("errorId", "No se encontró el restaurante");
			model.put(MODEL_NAME_SINGULAR, servicioRestaurante.get());
		} catch (NoHayPlatos e) {
			model.put(ERROR_NAME, "No hay platos en este restaurante");
		} catch (Exception e) {
			model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
		}
		addUserInfoToModel(model, request);
		model.put(DATOS_RESERVA, new DatosReserva());
		return new ModelAndView("home", model);
	}

	@PostMapping(path = "/restaurante/filtrarPlato")
	public ModelAndView filtrarPlato(@RequestParam("idRestaurante") Long idRestaurante,
			@RequestParam("precioPlato") String precioStr, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		try {
			Double precio = Double.valueOf(precioStr);
			Restaurante restaurante = servicioRestaurante.consultar(idRestaurante);
			List<Plato> platos = servicioPlato.consultarPlatoPorPrecio(precio);
			Map<Categoria, List<Plato>> platosPorCategoria = platos.stream()
					.collect(Collectors.groupingBy(Plato::getCategoria));
			List<Plato> platosRecomendados = platos.stream().filter(Plato::isEsRecomendado)
					.collect(Collectors.toList());

			model.put("platosPorCategoria", platosPorCategoria);
			model.put("platosRecomendados", platosRecomendados);
			model.put(MODEL_NAME_SINGULAR, restaurante);
			model.put(DATOS_RESERVA, new DatosReserva());
			addUserInfoToModel(model, request);
			return new ModelAndView(MODEL_NAME_SINGULAR, model);
		} catch (PlatoNoEncontrado e) {
			model.put(ERROR_NAME, "No existen platos");
		} catch (RestauranteNoEncontrado e) {
			model.put(ERROR_NAME, "No existe el restaurante");
		} catch (Exception e) {
			model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
		}
		model.put(DATOS_RESERVA, new DatosReserva());
		try {
			model.put(MODEL_NAME_SINGULAR, servicioRestaurante.consultar(idRestaurante));
		} catch (RestauranteNoEncontrado e) {
			model.put(ERROR_NAME, "No existe el restaurante");
		}
		addUserInfoToModel(model, request);
		return new ModelAndView(MODEL_NAME_SINGULAR, model);
	}

	@GetMapping(path = "/perfilRestaurante/{id}")
	public ModelAndView cargarPerfilRestaurante(@PathVariable("id") Long id, HttpServletRequest request) {
		ModelMap model = new ModelMap();
		long ganancia = 0;
		try {
			Restaurante restaurante = servicioRestaurante.consultar(id);
			List<Reserva> reservas = servicioReserva.buscarReservasDelRestaurante(restaurante.getId());
			ganancia = reservas.size()*5000;
			model.put(MODEL_NAME_RESERVAS, reservas);
			model.put("username", restaurante.getId());
		} catch (NoHayReservas e) {
			model.put(ERROR_NAME, "Todavía no tenes ninguna reserva.");
		} catch (Exception e) {
			model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
		}
		model.put(MODEL_NAME_GANANCIAS, ganancia);
		addUserInfoToModel(model, request);
		return new ModelAndView("perfil_restaurante", model);
	}

}
