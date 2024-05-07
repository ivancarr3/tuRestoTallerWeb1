package com.tallerwebi.dominio;

import javax.persistence.*;

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

    public Restaurante (Long id, String nombre, Double estrellas, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.estrellas = estrellas;
        this.direccion = direccion;
    }

    public Long getId() {return id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public Double getEstrellas() {return estrellas;}
    public void setEstrellas(Double estrellas) {this.estrellas = estrellas;}
    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}
}
