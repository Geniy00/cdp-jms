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

}
