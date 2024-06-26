package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Plato;
import com.tallerwebi.dominio.RepositorioRestaurante;
import com.tallerwebi.dominio.Restaurante;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioRestaurante")
@Transactional
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
        try {
            String hql = "FROM Restaurante WHERE id = :id";
            Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            return (Restaurante) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Restaurante> buscarPorEstrellasYOrdenar(Double estrellas, String tipoDeOrden) {

        String hql = "FROM Restaurante WHERE estrellas >= :estrellas ORDER BY estrellas " + tipoDeOrden;
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estrellas", estrellas);
        return query.getResultList();
    }

    
    @Override
    public List<Restaurante> buscarPorEstrellas(Double estrellas) {

        String hql = "FROM Restaurante WHERE estrellas >= :estrellas";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("estrellas", estrellas);
        return query.getResultList();
    }

    @Override
    public List<Restaurante> ordenarPorEstrellas(String tipoDeOrden) {

        String hql = "FROM Restaurante ORDER BY estrellas "+ tipoDeOrden;
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorDireccion(String direccion) {

        String hql = "FROM Restaurante WHERE LOWER(direccion) LIKE LOWER(:direccion)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("direccion", "%"+direccion.toLowerCase()+"%");

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorEspacio(Integer espacio) {

        String hql = "FROM Restaurante WHERE espacioDisponible >= :espacio";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("espacio", espacio);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> buscarPorNombre(String nombre) {
        String hql = "FROM Restaurante WHERE LOWER(nombre) LIKE LOWER(:nombre)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "%"+nombre.toLowerCase()+"%");
        return query.getResultList();
    }

    @Override
    public void actualizar(Restaurante restaurante) {
        this.sessionFactory.getCurrentSession().update(restaurante);
    }

    @Override
    public void eliminar(Restaurante restaurante) {
        this.sessionFactory.getCurrentSession().delete(restaurante);
    }
}
