package com.tallerwebi.dominio;

import javax.persistence.*;
import java.nio.file.Paths;

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

    public Restaurante() {}
    public Restaurante (Long id, String nombre, Double estrellas, String direccion, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.estrellas = estrellas;
        this.direccion = direccion;

        this.imagen = imagen; //!= null ? "/img/restaurant/".concat(imagen) : "/img/restaurant/restaurant.jpg";

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
}
