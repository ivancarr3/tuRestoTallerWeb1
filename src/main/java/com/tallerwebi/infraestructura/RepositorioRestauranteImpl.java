package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioRestaurante;
import com.tallerwebi.dominio.Restaurante;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioRestauranteImpl implements RepositorioRestaurante {

    private SessionFactory sessionFactory;

    public RepositorioRestauranteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Restaurante restaurante) {
        this.sessionFactory.getCurrentSession().save(restaurante);
    }

    @Override
    public Restaurante buscar(Long id) {

        String hql = "FROM restaurante WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Restaurante) query.getSingleResult();
    }

    @Override
    public List<Restaurante> buscarPorEstrellas(Integer estrellas) {

        String hql = "FROM restaurante WHERE estrellas = :estrellas";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estrellas", estrellas);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorDireccion(String direccion) {

        String hql = "FROM restaurante WHERE direccion = :direccion";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("direccion", direccion);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorNombre(String nombre) {

        String hql = "FROM restaurante WHERE nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", nombre);

        return query.getResultList();
    }

    @Override
    public void actualizar(Restaurante restaurante) {
        String hql = "UPDATE restaurante SET nombre = :nombre, estrellas = :estrellas, direccion = :direccion WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", restaurante.getNombre());
        query.setParameter("estrellas", restaurante.getEstrellas());
        query.setParameter("direccion", restaurante.getDireccion());
        query.setParameter("id", restaurante.getId());
        query.executeUpdate();
    }

    @Override
    public void eliminar(Restaurante restaurante) {
        String hql = "DELETE FROM restaurante WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", restaurante.getId());
        query.executeUpdate();
    }
}
