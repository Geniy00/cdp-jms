package ua.com.taxi.dao;

import org.springframework.stereotype.Repository;
import ua.com.taxi.entity.Booking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
public class BookingDaoImpl implements BookingDao {

    private static final String SELECT_BOOKING_BY_STATUS = "SELECT b FROM Booking b WHERE b.status=:status";
    private static final String COUNT_BOOKING_BY_STATUS = "SELECT count(b.id) FROM Booking b WHERE b.status=:status";

    @PersistenceContext
    EntityManager em;

    @Override
    public void create(Booking booking) {
        em.persist(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return em.merge(booking);
    }

    @Override
    public void delete(Booking booking) {
        em.remove(em.merge(booking));
    }

    @Override
    public Booking find(Long id) {
        return em.find(Booking.class, id);
    }

    @Override
    public List<Booking> findBookingByStatus(Booking.BookingStatus status) {
        TypedQuery<Booking> query = em.createQuery(SELECT_BOOKING_BY_STATUS, Booking.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public Long countBookingByStatus(Booking.BookingStatus status) {
        TypedQuery<Long> query = em.createQuery(COUNT_BOOKING_BY_STATUS, Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
}
