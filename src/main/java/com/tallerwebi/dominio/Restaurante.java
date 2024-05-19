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

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    public Restaurante() {}
    public Restaurante (Long id, String nombre, Double estrellas, String direccion, String imagen, Integer capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.estrellas = estrellas;
        this.direccion = direccion;
        this.imagen = imagen;
        this.capacidadMaxima = capacidadMaxima;
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
}
