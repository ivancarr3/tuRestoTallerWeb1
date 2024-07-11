package com.tallerwebi.dominio;



import javax.persistence.*;
import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Column(length = 100, nullable = false)
    private String imagen;

    @ManyToMany(mappedBy = "categorias")
    private List<Usuario> usuarios;

    public Categoria() {}

    public Categoria(Long id, String descripcion, String imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
