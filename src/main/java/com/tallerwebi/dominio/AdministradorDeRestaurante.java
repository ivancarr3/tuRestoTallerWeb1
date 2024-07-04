package com.tallerwebi.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AdministradorDeRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    public AdministradorDeRestaurante() {
    }

    public AdministradorDeRestaurante(Long id, String email, String password, Restaurante restaurante) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.restaurante = restaurante;
    }

    public AdministradorDeRestaurante(String email, String password, Restaurante restaurante) {
        this.email = email;
        this.password = password;
        this.restaurante = restaurante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Restaurante getRestaurante() {
        return this.restaurante;
    }

}
