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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean habilitado = false;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Plato> platos;

    private Double distancia;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Restaurante() {}

    public Restaurante(Long id, String nombre, Double estrellas, String direccion, String imagen,
                       Integer capacidadMaxima, Double latitud, Double longitud, boolean habilitado, Usuario usuario) {

        this.id = id;
        this.nombre = nombre;
        this.estrellas = estrellas;
        this.direccion = direccion;
        this.imagen = imagen;
        this.capacidadMaxima = capacidadMaxima;
        this.espacioDisponible = capacidadMaxima;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = 0.0;
        this.habilitado = habilitado;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Double estrellas) {
        this.estrellas = estrellas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

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

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
