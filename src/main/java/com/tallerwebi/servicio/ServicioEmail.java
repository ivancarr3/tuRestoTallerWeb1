package com.tallerwebi.servicio;

import com.tallerwebi.dominio.excepcion.MessagingException;

public interface ServicioEmail {
    void sendEmail(String to, String subject, String text) throws MessagingException;
}
