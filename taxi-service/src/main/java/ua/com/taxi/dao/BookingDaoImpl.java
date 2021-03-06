package ua.com.taxi.dao;

import org.joda.time.DateTime;
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

    private static final String SELECT_BOOKING = "SELECT b FROM Booking b ORDER BY b.bookingRequest.deliveryTime desc";
    private static final String SELECT_BOOKING_BY_STATUS =
            "SELECT b FROM Booking b WHERE b.status=:status ORDER BY b.bookingRequest.deliveryTime desc";
    private static final String COUNT_BOOKING_BY_STATUS = "SELECT count(b.id) FROM Booking b WHERE b.status=:status";

    private static final String SELECT_EXPIRED_BOOKINGS =
            "SELECT b FROM Booking b  WHERE b.bookingRequest.expiryTime < :dateTime "
                    + "AND (b.status = 'NEW' OR b.status = 'ASSIGNED' OR b.status = 'REVOKED' )";
    private static final String SELECT_BOOKING_WITH_EXPIRED_ASSIGNED_STATUS =
            "SELECT b FROM Booking b WHERE b.status = 'ASSIGNED' " + "AND b.assignToExpiryTime < :dateTime";

    @PersistenceContext
    EntityManager em;

    @Override
    public void create(final Booking booking) {
        em.persist(booking);
    }

    @Override
    public Booking update(final Booking booking) {
        return em.merge(booking);
    }

    @Override
    public void delete(final Booking booking) {
        em.remove(em.merge(booking));
    }

    @Override
    public Booking find(final Long id) {
        return em.find(Booking.class, id);
    }

    @Override
    public List<Booking> findBooking(final int limit) {
        final TypedQuery<Booking> query = em.createQuery(SELECT_BOOKING, Booking.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Booking> findBookingByStatus(final Booking.Status status, final int limit) {
        final TypedQuery<Booking> query = em.createQuery(SELECT_BOOKING_BY_STATUS, Booking.class);
        query.setParameter("status", status);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Long countBookingByStatus(final Booking.Status status) {
        final TypedQuery<Long> query = em.createQuery(COUNT_BOOKING_BY_STATUS, Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }

    @Override
    public List<Booking> findExpiredBookings() {
        final TypedQuery<Booking> query = em.createQuery(SELECT_EXPIRED_BOOKINGS, Booking.class);
        query.setParameter("dateTime", new DateTime());
        return query.getResultList();
    }

    @Override
    public List<Booking> findBookingWithExpiredAssignedStatus() {
        final TypedQuery<Booking> query = em.createQuery(SELECT_BOOKING_WITH_EXPIRED_ASSIGNED_STATUS, Booking.class);
        query.setParameter("dateTime", new DateTime());
        return query.getResultList();
    }
}
