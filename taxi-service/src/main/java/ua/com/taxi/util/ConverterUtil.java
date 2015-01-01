package ua.com.taxi.util;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.TsException;
import ua.com.taxi.entity.Booking;

/**
 * @author Geniy00
 */
public class ConverterUtil {

    public static Booking.Status convertBookingRequestStatusToBookingStatus(final BookingRequest.Status status)
            throws TsException {
        switch (status) {
        case ACCEPTED:
            return Booking.Status.ACCEPTED;
        case REJECTED:
            return Booking.Status.REJECTED;
        case REFUSED:
            return Booking.Status.REFUSED;
        case EXPIRED:
            return Booking.Status.EXPIRED;
        default:
            throw new TsException(TsException.Reason.ENUM_CONVERTING_ERROR, BookingRequest.Status.class.getSimpleName(),
                    status.name(), Booking.Status.class.getSimpleName());
        }

    }
}
