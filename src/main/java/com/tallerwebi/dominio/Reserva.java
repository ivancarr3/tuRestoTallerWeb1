package com.tallerwebi.dominio;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

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
    
    @Column(nullable = true)
    private String link;

    public Reserva() {}

    public Reserva(Long id, Restaurante restaurante, String nombre, String email, Integer numeroCelular,
                   Integer dni, Integer cantidadPersonas, Date fecha, Usuario usuario, String link) {
        this.id = id;
        this.restaurante = restaurante;
        this.nombre = nombre;
        this.email = email;
        this.numeroCelular = numeroCelular;
        this.dni = dni;
        this.cantidadPersonas = cantidadPersonas;
        this.fecha = fecha;
        this.usuario = usuario;
        this.link = link;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(Integer cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(Integer numeroCelular) { this.numeroCelular = numeroCelular; }
    public Integer getDni() { return dni; }
    public void setDni(Integer dni) { this.dni = dni; }
    public String getLink() {return link;}
    public void setLink(String link) { this.link = link;}
    

    public String getFechaFormateada() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.fecha);
    }
}
