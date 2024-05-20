package com.tallerwebi.dominio;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idRestaurante", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    private Integer cantidadPersonas;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private Long idUsuario;

    public Reserva() {}
    public Reserva (Long id, Restaurante restaurante, Integer cantidadPersonas, Date fecha, Long idUsuario) {
        this.id = id;
        this.restaurante = restaurante;
        this.cantidadPersonas = cantidadPersonas;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Integer getCantidadPersonas() {return cantidadPersonas;}
    public void setCantidadPersonas(Integer cantidadPersonas) {this.cantidadPersonas = cantidadPersonas;}
    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}
    public Long getIdUsuario() {return idUsuario;}
    public void setIdUsuario(Long idUsuario) {this.idUsuario = idUsuario;}
    public Restaurante getRestaurante() {
        return restaurante;
    }
    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String getFechaFormateada() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.fecha);
    }
}
