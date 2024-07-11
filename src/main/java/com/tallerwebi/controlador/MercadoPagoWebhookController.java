package com.tallerwebi.controlador;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.tallerwebi.servicio.ServicioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mercadopago")
public class MercadoPagoWebhookController {

    @Autowired
    private ServicioReserva servicioReserva;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        if ("payment".equals(payload.get("type"))) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Long paymentId = Long.valueOf(data.get("id").toString());
            try {
                String estadoPago = obtenerEstadoPago(paymentId);
                servicioReserva.actualizarEstadoPago(paymentId, estadoPago);
                return ResponseEntity.ok("Webhook handled successfully");
            } catch (MPException | MPApiException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook");
            }
        }
        return ResponseEntity.badRequest().body("Invalid webhook payload");
    }

    public String obtenerEstadoPago(Long paymentId) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken("TEST-231726774127987-061717-be04919a5b6480ff7181884b074dd77c-423795726");
        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(paymentId);
        return payment.getStatus();
    }
}

