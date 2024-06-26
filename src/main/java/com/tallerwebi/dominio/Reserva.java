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
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer numeroCelular;

    @Column(nullable = false)
    private Integer dni;

    @Column(nullable = false)
    private Integer cantidadPersonas;

    @Column(nullable = false)
    private Date fecha;

    public Reserva() {}

    public Reserva (Long id, Restaurante restaurante, String nombre, String email, Integer numCel,
            Integer dni, Integer cantidadPersonas, Date fecha) {
        this.id = id;
        this.restaurante = restaurante;
        this.nombre = nombre;
        this.email = email;
        this.numeroCelular = numCel;
        this.dni = dni;
        this.cantidadPersonas = cantidadPersonas;
        this.fecha = fecha;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Integer getCantidadPersonas() {return cantidadPersonas;}
    public void setCantidadPersonas(Integer cantidadPersonas) {this.cantidadPersonas = cantidadPersonas;}
    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(Integer numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }
}
