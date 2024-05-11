package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioReserva;
import com.tallerwebi.dominio.Reserva;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioReservaImpl implements RepositorioReserva {
    private final SessionFactory sessionFactory;

    public RepositorioReservaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Reserva reserva) {
        this.sessionFactory.getCurrentSession().save(reserva);
    }

    @Override
    public List<Reserva> buscarReservasDelUsuario(Long idUsuario) {
        String hql = "FROM Reserva WHERE idUsuario = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", idUsuario);
        return query.getResultList();
    }

    @Override
    public Reserva buscarReserva(Long id) {
        String hql = "FROM Reserva WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        return (Reserva) query.getSingleResult();
    }

    @Override
    public void actualizar(Reserva reserva) {
        String hql = "UPDATE Reserva SET fecha = :fecha, cantidadPersonas = :cantidadPersonas WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("fecha", reserva.getFechaFormateada());
        query.setParameter("cantidadPersonas", reserva.getCantidadPersonas());
        query.setParameter("id", reserva.getId());
        query.executeUpdate();
    }

    @Override
    public void eliminar(Reserva reserva) {
        String hql = "DELETE FROM Reserva WHERE id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", reserva.getId());
        query.executeUpdate();
    }

}
