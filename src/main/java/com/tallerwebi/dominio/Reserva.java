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
    private Long idRestaurante;

    @Column(nullable = false)
    private Integer cantidadPersonas;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private Long idUsuario;

    public Reserva() {}
    public Reserva (Long id, Long idRestaurante, Integer cantidadPersonas, Date fecha, Long idUsuario) {
        this.id = id;
        this.idRestaurante = idRestaurante;
        this.cantidadPersonas = cantidadPersonas;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Long getIdRestaurante() {return idRestaurante;}
    public void setIdRestaurante(Long idRestaurante) {this.idRestaurante = idRestaurante;}
    public Integer getCantidadPersonas() {return cantidadPersonas;}
    public void setCantidadPersonas(Integer cantidadPersonas) {this.cantidadPersonas = cantidadPersonas;}
    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}
    public Long getIdUsuario() {return idUsuario;}
    public void setIdUsuario(Long idUsuario) {this.idUsuario = idUsuario;}
    public String getFechaFormateada() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.fecha);
    }
}
