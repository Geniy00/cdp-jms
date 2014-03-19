package ua.com.taxi.service;

import ua.com.taxi.entity.Booking;

import java.util.List;

/**
 * @author Geniy00
 */
public interface BookingService {

    Booking saveOrUpdate(Booking booking);

    void delete(Booking booking);

    Booking find(Long id);

    Booking findFreeBooking();

    Booking assignBooking(Long bookingId);

    Booking unassignBooking(Long bookingId);

    Booking acceptBooking(Long bookingId);

    Booking rejectBooking(Long bookingId);

    Booking refuseBooking(Long bookingId, String reason);

    Boolean sendTextMessageToFailQueue(String xmlBookingRequestMessage);

    public Long countActualBookings();

    public List<Booking> findExpiredBookings();

}
