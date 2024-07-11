package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository("repositorioReserva")
@Transactional
public class RepositorioReservaImpl implements RepositorioReserva {

    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioReservaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Reserva reserva) {
        this.sessionFactory.getCurrentSession().save(reserva);
    }

    @Override
    public List<Reserva> buscarReservasDelUsuario(Long idUsuario) {
        String hql = "FROM Reserva WHERE idUsuario = :idUsuario AND fecha >= CURRENT_DATE";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        return (List<Reserva>) query.getResultList();
    }

    @Override
    public List<Reserva> buscarReservasDelUsuarioPasadas(Long idUsuario) {
        String hql = "FROM Reserva WHERE idUsuario = :idUsuario AND fecha <= CURRENT_DATE";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        return (List<Reserva>) query.getResultList();
    }

    @Override
    public List<Reserva> buscarReservasDelRestaurante(Long idRestaurante) {
        String hql = "FROM Reserva WHERE idRestaurante = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", idRestaurante);
        return (List<Reserva>) query.getResultList();
    }

    @Override
    public Reserva findByPaymentId(String paymentId) {
        String hql = "FROM Reserva WHERE paymentId = :paymentId";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("paymentId", paymentId);
        return (Reserva) query.getSingleResult();
    }

    @Override
    public List<Reserva> buscarTodasLasReservas() {
        String hql = "FROM Reserva";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return (List<Reserva>) query.getResultList();
    }

    @Override
    public Reserva buscarReserva(Long id) {
        try {
            String hql = "FROM Reserva WHERE id = :id";
            Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
            query.setParameter("id", id);
            return (Reserva) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<String> buscarEmailDeUsuariosPorRestaurante(Long idRestaurante){
        String hql = "SELECT r.email FROM Reserva r WHERE r.restaurante.id = :idRestaurante";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idRestaurante", idRestaurante);
        return (List<String>) query.getResultList();
    }

    @Override
    public void actualizar(Reserva reserva) {
        this.sessionFactory.getCurrentSession().update(reserva);
    }

    @Override
    public void eliminar(Reserva reserva) {
        this.sessionFactory.getCurrentSession().delete(reserva);
    }

    @Override
    public List<Reserva> getReservasPorRestaurante(Long idRestaurante) {
        String hql = "FROM Reserva WHERE restaurante.id = :idRestaurante";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idRestaurante", idRestaurante);
        return (List<Reserva>) query.getResultList();
    }
}
