package ua.com.taxi.service;

import org.joda.time.DateTime;
import ua.com.taxi.entity.Booking;

/**
 * @author Geniy00
 */
public interface BookingService {

    Booking saveOrUpdate(Booking booking);

    void delete(Booking booking);

    Booking find(Long id);

    Booking findFreeBooking();

    void assignBooking(Booking booking, DateTime dateTime);

    void unassignBooking(Booking booking);

    Booking acceptBooking(Long bookingId);

    Booking rejectBooking(Long bookingId);

    Boolean refuseBooking(Long bookingId, String reason);

    Boolean sendTextMessageToFailQueue(String xmlBookingRequestMessage);

    public Long countActualBookings();

}
