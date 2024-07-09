package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.RepositorioCategoria;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository("repositorioCategoria")
@Transactional
public class RepositorioCategoriaImpl implements RepositorioCategoria {

    private final SessionFactory sessionFactory;

    public RepositorioCategoriaImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public List<Categoria> get(){
        try{
            String hql = "FROM Categoria";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Categoria buscarCategoria(Long id){
        try{
            String hql = "FROM Categoria WHERE id = :id";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            return (Categoria) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Categoria> buscarCategoriasPorListDeIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String hql = "FROM Categoria WHERE id IN (:ids)";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameterList("ids", ids);
        return query.list();
    }

    @Override
    public void guardar(Categoria categoria) {
        this.sessionFactory.getCurrentSession().save(categoria);
    }

    @Override
    public void modificar(Categoria categoria) {
        this.sessionFactory.getCurrentSession().update(categoria);
    }

    @Override
    public void eliminar(Categoria categoria) {
        this.sessionFactory.getCurrentSession().delete(categoria);
    }
}
