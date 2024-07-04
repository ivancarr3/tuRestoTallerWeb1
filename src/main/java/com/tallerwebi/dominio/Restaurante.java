package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double estrellas;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String imagen;

    @Column(nullable = false)
    private Integer capacidadMaxima;

    @Column(nullable = false)
    private Integer espacioDisponible;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plato> platos;

    public Restaurante() {}

    public Restaurante (Long id, String nombre, Double estrellas, String direccion, String imagen, Integer capacidadMaxima, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.estrellas = estrellas;
        this.direccion = direccion;
        this.imagen = imagen;
        this.capacidadMaxima = capacidadMaxima;
        this.espacioDisponible = capacidadMaxima;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public Double getEstrellas() {return estrellas;}
    public void setEstrellas(Double estrellas) {this.estrellas = estrellas;}
    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}
    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}
    public Integer getCapacidadMaxima() {return capacidadMaxima;}
    public void setCapacidadMaxima(Integer capacidadMaxima) {this.capacidadMaxima = capacidadMaxima;}
    public List<Reserva> getReservas() {return reservas;}
    public void setReservas(List<Reserva> reservas) {this.reservas = reservas;}
    public Integer getEspacioDisponible() {
        return espacioDisponible;
    }
    public void setEspacioDisponible(Integer espacioDisponible) {
        this.espacioDisponible = espacioDisponible;
    }
    public List<Plato> getPlatos() {
        return platos;
    }
    public void setPlatos(List<Plato> platos) {
        this.platos = platos;
    }
    public Double getLongitud() {
        return longitud;
    }
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    public Double getLatitud() {
        return latitud;
    }
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
}
