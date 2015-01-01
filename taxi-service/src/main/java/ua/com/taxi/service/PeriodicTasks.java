package ua.com.taxi.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.entity.Booking;

import java.util.List;

/**
 * @author Geniy00
 */
@Component
public class PeriodicTasks {

    public static final Logger LOG = Logger.getLogger(PeriodicTasks.class);

    @Autowired
    BookingService bookingService;

    /**
     * Move booking to expired time when current taxi service can't execute any actions on the booking,
     * and current booking was delegated to another taxi service by router module
     */
    public void moveBookingsToExpired() {
        final List<Booking> expiredBookings = bookingService.findExpiredBookings();
        for (final Booking expiredBooking : expiredBookings) {
            expiredBooking.setStatus(Booking.Status.EXPIRED);
            bookingService.saveOrUpdate(expiredBooking);
        }

        if (expiredBookings.size() > 0) {
            LOG.warn(expiredBookings.size() + " bookings was expired automatically");
        }
    }

    /**
     * Revoked automatically when operator's assign time is expired
     */
    public void revokeBookingAutomatically() {
        final List<Booking> expiredAndAssignedBookings = bookingService.findExpiredAndAssignedBookings();
        for (Booking booking : expiredAndAssignedBookings) {
            booking.setStatus(Booking.Status.REVOKED);
            booking.setAssignToExpiryTime(null);
            bookingService.saveOrUpdate(booking);
        }
        if (expiredAndAssignedBookings.size() > 0) {
            LOG.info(String.format("%d booking was revoked automatically", expiredAndAssignedBookings.size()));
        }
    }

}
