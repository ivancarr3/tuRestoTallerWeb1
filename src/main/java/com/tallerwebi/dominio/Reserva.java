package com.tallerwebi.dominio;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer idRestaurante;

    @Column(nullable = false)
    private Integer cantidadPersonas;

    @Column(nullable = false)
    private Date hora;

    public Reserva() {}
    public Reserva (Long id, Integer idRestaurante, Integer cantidadPersonas, Date hora) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.cantidadPersonas = cantidadPersonas;
        this.hora = hora;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Integer getIdRestaurante() {return idRestaurante;}
    public void setIdRestaurante(Integer idRestaurante) {this.idRestaurante = idRestaurante;}
    public Integer getCantidadPersonas() {return cantidadPersonas;}
    public void setCantidadPersonas(Integer cantidadPersonas) {this.cantidadPersonas = cantidadPersonas;}
    public Date getHora() {return hora;}
    public void setHora(Date hora) {this.hora = hora;}

    public String getHoraFormateada() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.hora);
    }
}
