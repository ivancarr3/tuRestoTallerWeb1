package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Repository("repositorioCategoria")
@Transactional
public class RepositorioCategoriaImpl implements RepositorioCategoria {

    private final SessionFactory sessionFactory;

    public RepositorioCategoriaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Categoria buscarCategoria(Long id) {
        try {
            String hql = "FROM Categoria c WHERE c.id = :id";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            return (Categoria) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void guardar(Categoria categoria) {
        this.sessionFactory.getCurrentSession().save(categoria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Categoria> obtenCategorias() {
        String hql = "FROM Categoria";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return (List<Categoria>) query.list();
    }
}
