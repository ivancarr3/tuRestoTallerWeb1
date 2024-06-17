package com.tallerwebi.controlador;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class DatosReserva {
    private Long idRestaurante;
    private String nombreForm;
    private String emailForm;
    private Integer numForm;
    private Integer dniForm;
    private Integer cantPersonas;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaForm;

    public DatosReserva() {}
    public DatosReserva(Long idRestaurante, String nombreForm, String emailForm, Integer numForm, Integer dniForm, Integer cantPersonas, Date fechaForm) {
        this.idRestaurante = idRestaurante;
        this.nombreForm = nombreForm;
        this.emailForm = emailForm;
        this.numForm = numForm;
        this.dniForm = dniForm;
        this.cantPersonas = cantPersonas;
        this.fechaForm = fechaForm;
    }

    public Long getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Long idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNombreForm() {
        return nombreForm;
    }

    public void setNombreForm(String nombreForm) {
        this.nombreForm = nombreForm;
    }

    public String getEmailForm() {
        return emailForm;
    }

    public void setEmailForm(String emailForm) {
        this.emailForm = emailForm;
    }

    public Integer getNumForm() {
        return numForm;
    }

    public void setNumForm(Integer numForm) {
        this.numForm = numForm;
    }

    public Integer getDniForm() {
        return dniForm;
    }

    public void setDniForm(Integer dniForm) {
        this.dniForm = dniForm;
    }

    public Integer getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(Integer cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public Date getFechaForm() {
        return fechaForm;
    }

    public void setFechaForm(Date fechaForm) {
        this.fechaForm = fechaForm;
    }
}
