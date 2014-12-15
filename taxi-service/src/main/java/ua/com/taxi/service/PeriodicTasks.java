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
        List<Booking> expiredBookings = bookingService.findExpiredBookings();
        for (Booking expiredBooking : expiredBookings) {
            expiredBooking.setStatus(Booking.BookingStatus.EXPIRED);
            bookingService.saveOrUpdate(expiredBooking);
        }

        if (expiredBookings.size() > 0) {
            LOG.warn(expiredBookings.size() + " bookings was expired automatically");
        }
    }

    /**
     * Unassign automatically when operator's assign time expired
     */
    public void unassignAutomatically() {
        List<Booking> assignedStatusExpired = bookingService.findBookingWithExpiredAssignedStatus();
        for (Booking booking : assignedStatusExpired) {
            booking.setStatus(Booking.BookingStatus.REVOKED);
            booking.setAssignToExpiryTime(null);
            bookingService.saveOrUpdate(booking);
        }
        if (assignedStatusExpired.size() > 0) {
            LOG.info("Automatically unassigned " + assignedStatusExpired.size() + " bookings");
        }
    }

}
