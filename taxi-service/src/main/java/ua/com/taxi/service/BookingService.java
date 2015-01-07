package ua.com.taxi.service;

import com.epam.cdp.core.entity.TsException;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.ClientDetails;

import java.util.List;

/**
 * @author Geniy00
 */
public interface BookingService {

    Booking saveOrUpdate(Booking booking);

    void delete(Booking booking);

    Booking find(Long id);

    com.google.common.base.Optional<Booking> findFreeBooking();

    Booking assignBooking(Long bookingId) throws TsException;

    Booking revokeBooking(Long bookingId) throws TsException;

    Booking acceptBooking(Long bookingId) throws TsException;

    ClientDetails getClientDetails(Long bookingId) throws TsException;

    Booking rejectBooking(Long bookingId) throws TsException;

    Booking refuseBooking(Long bookingId, String reason) throws TsException;

    Long countActualBookings();

    List<Booking> findExpiredBookings();

    List<Booking> findExpiredAndAssignedBookings();

    List<Booking> findBookings(int limit);

    List<Booking> findBookingByStatus(Booking.Status status, int limit);
}
