package ua.com.taxi.service;

import org.joda.time.DateTime;
import ua.com.taxi.entity.Booking;

/**
 * @author Geniy00
 */
public interface BookingService {

    Booking saveOrUpdate(Booking booking);

    void delete(Booking booking);

    Booking find(String id);

    Booking findFreeBooking();

    void assignBooking(Booking booking, DateTime dateTime);

    void unassignBooking(Booking booking);

    Boolean acceptBooking(Booking booking);

    Boolean rejectBooking(Booking booking);

    Boolean refuseBooking(Booking booking, String reason);

    Boolean sendTextMessageToFailQueue(String xmlBookingRequestMessage);

    public Long countActualBookings();

}
