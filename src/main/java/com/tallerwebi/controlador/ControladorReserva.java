package com.tallerwebi.controlador;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioMercadoPago;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import com.tallerwebi.servicio.ServicioUsuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ControladorReserva {

	private final ServicioRestaurante servicioRestaurante;
	private final ServicioReserva servicioReserva;
	private final ServicioUsuario servicioUsuario;
	private final ServicioMercadoPago servicioMercadoPago;

	private static final String VIEW_NAME = "errReserva";
	private static final String ERROR_NAME = "error";
	private static final String RESERVA_EXITOSA_VIEW = "reserva_exitosa";
	private static final String REDIRECT_HOME_VIEW = "redirect:/home";
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

	public ControladorReserva(ServicioUsuario servicioUsuario, ServicioRestaurante servicioRestaurante, ServicioReserva servicioReserva, ServicioMercadoPago servicioMercadoPago) {
		this.servicioRestaurante = servicioRestaurante;
		this.servicioReserva = servicioReserva;
		this.servicioMercadoPago = servicioMercadoPago;
		this.servicioUsuario = servicioUsuario;
	}

	@PostMapping(path = "/reservar")

	public ModelAndView reservar(@ModelAttribute DatosReserva datosReserva, HttpServletRequest request) {

		ModelMap model = new ModelMap();
		try {
			validarDatosReserva(datosReserva.getNombreForm(), datosReserva.getEmailForm(), datosReserva.getNumForm(),
					datosReserva.getDniForm(), datosReserva.getCantPersonas(), datosReserva.getFechaForm());

			Restaurante restauranteEncontrado = servicioRestaurante.consultar(datosReserva.getIdRestaurante());


			Usuario usuario = obtenerIdUsuarioAutenticado(request);


			Reserva reserva = servicioReserva.crearReserva(restauranteEncontrado, datosReserva.getNombreForm(),
					datosReserva.getEmailForm(), datosReserva.getNumForm(), datosReserva.getDniForm(),
					datosReserva.getCantPersonas(), datosReserva.getFechaForm(), usuario);

			String idPago = servicioMercadoPago.armarPago(restauranteEncontrado, reserva, 5000);

			model.put("urlpago", "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=" + idPago);
			return new ModelAndView(RESERVA_EXITOSA_VIEW, model);
		} catch (RestauranteNoEncontrado | DatosInvalidosReserva | EspacioNoDisponible e) {
			model.put(ERROR_NAME, e.getMessage());

		} catch (Exception e) {

			model.put(ERROR_NAME, "Error del servidor: " + e.getMessage());
		}
		return new ModelAndView(VIEW_NAME, model);
	}

	private Usuario obtenerIdUsuarioAutenticado(HttpServletRequest request) throws NoExisteUsuario {
		HttpSession session = request.getSession(false);
		String email = (String) session.getAttribute("email");
		return servicioUsuario.buscar(email);
	}

	@GetMapping(path = "/reservar")
	public ModelAndView getRequest() {
		ModelMap model = new ModelMap();
		model.addAttribute("datosReserva", new DatosReserva());
		return new ModelAndView(REDIRECT_HOME_VIEW, model);
	}

	private void validarDatosReserva(String nombreForm, String emailForm, Integer numForm, Integer dniForm,

									 Integer cantPersonas, Date fechaForm) throws DatosInvalidosReserva {

		if (nombreForm == null || nombreForm.isEmpty() || emailForm == null || emailForm.isEmpty() || numForm == null
				|| dniForm == null || cantPersonas == null || fechaForm == null) {
			throw new DatosInvalidosReserva();
		}

		Date fechaActual = new Date();

		if (fechaForm.before(fechaActual)) {
			throw new DatosInvalidosReserva();
		}

		if (!emailForm.matches(EMAIL_REGEX)) {
			throw new DatosInvalidosReserva();
		}
	}
}
