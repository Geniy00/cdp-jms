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

    Booking find(String id);

    List<Booking> findBookingByStatus(Booking.BookingStatus status);

    Long countBookingByStatus(Booking.BookingStatus status);

}
