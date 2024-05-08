package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.RepositorioPlato;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioPlatoImpl implements RepositorioPlato {

    private final SessionFactory sessionFactory;

    public RepositorioPlatoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarPlato(Plato plato) {
        this.sessionFactory.getCurrentSession().save(plato);
    }

    @Override
    public List<Plato> get() {
        String hql = "FROM Plato";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Plato buscar(Long id) {

        String hql = "FROM Plato WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Plato) query.getSingleResult();
    }

    @Override
    public List<Plato> buscarPlatoPorPrecio(Integer precio) {

        String hql = "FROM Plato WHERE precio = :precio";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("precio", precio);

        return query.getResultList();
    }

    @Override
    public List<Plato> buscarPlatoPorNombre(String nombre) {

        String hql = "FROM Plato WHERE nombre = :nombre";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", nombre);

        return query.getResultList();
    }

    @Override
    public void modificarPlato(Plato plato) {
        String hql = "UPDATE Plato SET nombre = :nombre, precio = :precio WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", plato.getNombre());
        query.setParameter("precio", plato.getPrecio());
        query.setParameter("id", plato.getId());
        query.executeUpdate();
    }

    @Override
    public void eliminarPlato(Plato plato) {
        String hql = "DELETE FROM Plato WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", plato.getId());
        query.executeUpdate();
    }
}
