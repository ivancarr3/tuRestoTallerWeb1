package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioRestaurante;
import com.tallerwebi.dominio.Restaurante;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioRestauranteImpl implements RepositorioRestaurante {

    private final SessionFactory sessionFactory;

    public RepositorioRestauranteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Restaurante restaurante) {
        this.sessionFactory.getCurrentSession().save(restaurante);
    }

    @Override
    public List<Restaurante> get() {

        String hql = "FROM Restaurante";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Restaurante buscar(Long id) {

        String hql = "FROM Restaurante WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Restaurante) query.getSingleResult();
    }

    @Override
    public List<Restaurante> buscarPorEstrellas(Double estrellas) {

        String hql = "FROM Restaurante WHERE estrellas = :estrellas";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estrellas", estrellas);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorDireccion(String direccion) {

        String hql = "FROM Restaurante WHERE direccion = :direccion";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("direccion", direccion);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorNombre(String nombre) {

        String hql = "FROM Restaurante WHERE nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", nombre);

        return query.getResultList();
    }

    @Override
    public void actualizar(Restaurante restaurante) {
        String hql = "UPDATE Restaurante SET nombre = :nombre, estrellas = :estrellas, direccion = :direccion WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", restaurante.getNombre());
        query.setParameter("estrellas", restaurante.getEstrellas());
        query.setParameter("direccion", restaurante.getDireccion());
        query.setParameter("id", restaurante.getId());
        query.executeUpdate();
    }

    @Override
    public void eliminar(Restaurante restaurante) {
        String hql = "DELETE FROM Restaurante WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", restaurante.getId());
        query.executeUpdate();
    }
}
