package ua.com.taxi.dao;

import ua.com.taxi.entity.Booking;

import java.util.List;

/**
 * @author Geniy00
 */
public interface BookingDao {

    void create(Booking booking);

    Booking update(Booking booking);

    void delete(Booking booking);

    Booking find(Long id);

    List<Booking> findBooking(int limit);

    List<Booking> findBookingByStatus(Booking.BookingStatus status, int limit);

    Long countBookingByStatus(Booking.BookingStatus status);

    List<Booking> findExpiredBookings();
}
