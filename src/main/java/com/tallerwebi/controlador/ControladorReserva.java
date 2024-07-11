package com.tallerwebi.controlador;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Restaurante;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.DatosInvalidosReserva;
import com.tallerwebi.dominio.excepcion.EmailInvalido;
import com.tallerwebi.dominio.excepcion.EspacioNoDisponible;
import com.tallerwebi.dominio.excepcion.FechaAnterior;
import com.tallerwebi.dominio.excepcion.NoExisteUsuario;
import com.tallerwebi.dominio.excepcion.RestauranteNoEncontrado;
import com.tallerwebi.servicio.ServicioMercadoPago;
import com.tallerwebi.servicio.ServicioReserva;
import com.tallerwebi.servicio.ServicioRestaurante;
import com.tallerwebi.servicio.ServicioUsuario;

@Controller
public class ControladorReserva {

	private final ServicioRestaurante servicioRestaurante;
	private final ServicioReserva servicioReserva;
	private final ServicioUsuario servicioUsuario;
	private final ServicioMercadoPago servicioMercadoPago;

	private static final String ERROR_NAME = "errorForm";
	private static final String RESERVA_EXITOSA_VIEW = "reserva_exitosa";
	private static final String REDIRECT_HOME_VIEW = "redirect:/home";
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

	public ControladorReserva(ServicioUsuario servicioUsuario, ServicioRestaurante servicioRestaurante, ServicioReserva servicioReserva, ServicioMercadoPago servicioMercadoPago) {
		this.servicioRestaurante = servicioRestaurante;
		this.servicioReserva = servicioReserva;
		this.servicioMercadoPago = servicioMercadoPago;
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

	@PostMapping(path = "/reservar")
	public ModelAndView reservar(@ModelAttribute DatosReserva datosReserva, HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
			String linkDePago = "https://sandbox.mercadopago.com.ar/checkout/v1/redirect?pref_id=" + idPago;

			reserva.setLink(linkDePago);
			servicioReserva.actualizar(reserva);
			model.put("urlpago", linkDePago);

			addUserInfoToModel(model, request);
			return new ModelAndView(RESERVA_EXITOSA_VIEW, model);
		} catch (RestauranteNoEncontrado | DatosInvalidosReserva | FechaAnterior | EmailInvalido | EspacioNoDisponible e) {
			redirectAttributes.addFlashAttribute(ERROR_NAME, e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ERROR_NAME, "Error del servidor: " + e.getMessage());
		}
		addUserInfoToModel(model, request);
		return new ModelAndView("redirect:/restaurante/" + datosReserva.getIdRestaurante());
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

									 Integer cantPersonas, Date fechaForm) throws DatosInvalidosReserva, FechaAnterior, EmailInvalido {

		if (nombreForm == null || nombreForm.isEmpty() || emailForm == null || emailForm.isEmpty() || numForm == null
				|| dniForm == null || cantPersonas == null || fechaForm == null) {
			throw new DatosInvalidosReserva();
		}

		Date fechaActual = new Date();

		if (fechaForm.before(fechaActual)) {
			throw new FechaAnterior();
		}

		if (!emailForm.matches(EMAIL_REGEX)) {
			throw new EmailInvalido();
		}
	}
}
