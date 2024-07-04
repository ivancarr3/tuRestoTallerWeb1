package com.tallerwebi.infraestructura;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.AdministradorDeRestaurante;
import com.tallerwebi.dominio.RepositorioAdministradorRestaurante;

@Repository("repositorioAdministradorRestaurante")
@Transactional
public class RepositorioAdministradorRestauranteImpl implements RepositorioAdministradorRestaurante {

    private final SessionFactory sessionFactory;

    public RepositorioAdministradorRestauranteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AdministradorDeRestaurante buscarAdministradorDeRestaurante(String email, String pass) {

        try {
            String hql = "FROM AdministradorDeRestaurante a WHERE a.email = :email and a.password = :pass";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("email", email);
            query.setParameter("pass", pass);
            return (AdministradorDeRestaurante) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public AdministradorDeRestaurante obtenerAdministradorDeRestaurantePorId(Long id) {
        String hql = "FROM AdministradorDeRestaurante a WHERE a.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        return (AdministradorDeRestaurante) query.getSingleResult();
    }

}
