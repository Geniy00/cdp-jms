package ua.com.taxi.service;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.TsException;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.taxi.dao.BookingDao;
import ua.com.taxi.entity.Booking;

import java.util.List;
import java.util.Random;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = Logger.getLogger(BookingServiceImpl.class);

    private static final int MAX_BOOKING_SELECT_LIMIT = 100;

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private RouterRestClient restClient;

    @Value("${local.assign.expiry.time}")
    private Integer ASSIGN_EXPIRY_TIME;

    private final Random random = new Random();

    @Override
    public Booking saveOrUpdate(Booking booking) {
        return bookingDao.update(booking);
    }

    @Override
    public void delete(Booking booking) {
        bookingDao.delete(booking);
    }

    @Override
    public Booking find(Long id) {
        return bookingDao.find(id);
    }

    @Override
    public Optional<Booking> findFreeBooking() {
        final List<Booking> bookings = bookingDao.findBookingByStatus(Booking.Status.NEW, MAX_BOOKING_SELECT_LIMIT);
        bookings.addAll(bookingDao.findBookingByStatus(Booking.Status.REVOKED, MAX_BOOKING_SELECT_LIMIT));

        final int size = bookings.size();
        if (size > 0) {
            final int index = random.nextInt(size);
            return Optional.of(bookings.get(index));
        } else {
            return Optional.absent();
        }
    }

    /**
     * Assign booking to an dispatcher to prevent possibility to process booking concurrently
     *
     * @param bookingId
     * @return updated booking object from DB
     * @throws TsException - can't change current status
     */
    @Override
    public Booking assignBooking(final Long bookingId) throws TsException {
        final Booking booking = bookingDao.find(bookingId);
        final Booking.Status newStatus = Booking.Status.ASSIGNED;
        validateApplyingNewStatus(booking, newStatus);

        booking.setStatus(newStatus);
        booking.setAssignToExpiryTime(new DateTime().plusMinutes(ASSIGN_EXPIRY_TIME));
        return bookingDao.update(booking);
    }

    @Override
    public Booking revokeBooking(final Long bookingId) throws TsException {
        final Booking booking = bookingDao.find(bookingId);
        final Booking.Status newStatus = Booking.Status.REVOKED;
        validateApplyingNewStatus(booking, newStatus);

        booking.setStatus(newStatus);
        booking.setAssignToExpiryTime(null);
        return bookingDao.update(booking);
    }

    @Override
    public Booking acceptBooking(final Long bookingId) throws TsException {
        return requestNewStatusForBooking(bookingId, Booking.Status.ACCEPTED, "");
    }

    @Override
    public Booking rejectBooking(final Long bookingId) throws TsException {
        return requestNewStatusForBooking(bookingId, Booking.Status.REJECTED, "");
    }

    @Override
    public Booking refuseBooking(final Long bookingId, final String reason) throws TsException {
        return requestNewStatusForBooking(bookingId, Booking.Status.REFUSED, reason);
    }

    @Override
    public Long countActualBookings() {
        final long newCount = bookingDao.countBookingByStatus(Booking.Status.NEW);
        final long unassignedCount = bookingDao.countBookingByStatus(Booking.Status.REVOKED);
        return newCount + unassignedCount;
    }

    @Override
    public List<Booking> findExpiredBookings() {
        return bookingDao.findExpiredBookings();
    }

    @Override
    public List<Booking> findExpiredAndAssignedBookings() {
        return bookingDao.findBookingWithExpiredAssignedStatus();
    }

    @Override
    public List<Booking> findBookings(final int limit) {
        return bookingDao.findBooking(limit);
    }

    @Override
    public List<Booking> findBookingByStatus(final Booking.Status status, final int limit) {
        return bookingDao.findBookingByStatus(status, limit);
    }

    private Booking requestNewStatusForBooking(final Long bookingId, final Booking.Status newStatus,
            final String reason) throws TsException {
        final Booking booking = bookingDao.find(bookingId);
        validateApplyingNewStatus(booking, newStatus);

        final BookingRequestEnum.Action action = getActionByStatus(newStatus);

        final Booking.Status status = restClient.executeActionRequest(booking, action, reason);

        if (newStatus == status) {
            booking.setStatus(status);
            return bookingDao.update(booking);
        }

        return booking;
    }

    private BookingRequestEnum.Action getActionByStatus(final Booking.Status newStatus) throws TsException {
        final BookingRequestEnum.Action action;
        switch (newStatus){
        //TODO: review it
        case ACCEPTED:
            action = BookingRequestEnum.Action.ACCEPT;
            break;

        case REJECTED:
            action = BookingRequestEnum.Action.REJECT;
            break;

        case REFUSED:
            action = BookingRequestEnum.Action.REFUSE;
            break;
        default:
            throw new TsException(TsException.Reason.ACTION_IS_UNAVAILABLE, "for status " + newStatus);
        }
        return action;
    }

    private void validateApplyingNewStatus(final Booking booking, final Booking.Status newStatus)
            throws TsException {
        if (!canChangeStatusTo(booking, newStatus)) {
            final String message = String.format("Current booking state can't be changed from %s to %s",
                    booking.getStatus(), newStatus);
            LOG.error(message);
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, message);
        }
    }

    private Boolean canChangeStatusTo(final Booking booking, final Booking.Status newStatus) {
        final Booking.Status currentStatus = booking.getStatus();
        switch (newStatus) {
        case NEW:
            return isNotExpired(booking);
        case ASSIGNED:
            return (currentStatus == Booking.Status.NEW || currentStatus == Booking.Status.REVOKED) && isNotExpired(
                    booking);

        case REVOKED:
        case ACCEPTED:
        case REJECTED:
            return currentStatus == Booking.Status.ASSIGNED && isNotExpired(booking);

        case REFUSED:
            return currentStatus == Booking.Status.ACCEPTED || currentStatus == Booking.Status.REJECTED;

        case EXPIRED:
            return !isNotExpired(booking);

        default:
            LOG.error("Unexpected new status");
            throw new RuntimeException("Unexpected new status");
        }
    }

    private Boolean isNotExpired(final Booking booking) {
        return booking.getBookingRequest().getExpiryTime().isAfter(new DateTime());
    }


}
