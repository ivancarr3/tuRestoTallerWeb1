package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.RepositorioPlato;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioPlato")
@Transactional
public class RepositorioPlatoImpl implements RepositorioPlato {

    private final SessionFactory sessionFactory;

    @Autowired
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
    public List<Plato> getPlatosDeRestaurante(Long idRestaurante) {
        String hql = "FROM Plato WHERE restaurante.id = :idRestaurante";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idRestaurante", idRestaurante);
        return query.getResultList();
    }

    @Override
    public Plato buscar(Long id) {
        try {
            String hql = "FROM Plato WHERE id = :id";
            Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);

            return (Plato) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Plato> getPlatosAgrupadosPorCategoria() {
        String hql = "FROM Plato p JOIN FETCH p.categoria ORDER BY p.categoria.id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public List<Plato> buscarPlatoPorPrecio(Double precio) {

        String hql = "FROM Plato WHERE precio >= :precio";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("precio", precio);

        return query.getResultList();
    }

    @Override
    public List<Plato> getPlatosPorCategoria(String categoria) {
        String hql = "FROM Plato p WHERE p.restaurante.habilitado = TRUE AND p.categoria.descripcion = :categoria ORDER BY p.precio DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("categoria", categoria);
        query.setMaxResults(4);
        return query.getResultList();
    }

    @Override
    public List<Plato> buscarPlatoPorNombre(String nombre) {

        String hql = "FROM Plato WHERE LOWER(nombre) LIKE LOWER(:nombre)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "%"+nombre.toLowerCase()+"%");

        return query.getResultList();
    }

    @Override
    public List<Plato> ordenarPorPrecio(String tipoDeOrden) {

        String hql = "FROM Plato ORDER BY precio "+ tipoDeOrden;
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public void modificarPlato(Plato plato) {
        sessionFactory.getCurrentSession().update(plato);
    }

    @Override
    public void eliminarPlato(Plato plato) {
        sessionFactory.getCurrentSession().delete(plato);
    }
}
