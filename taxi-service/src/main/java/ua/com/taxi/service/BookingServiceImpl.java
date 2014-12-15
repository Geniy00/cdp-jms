package ua.com.taxi.service;

import com.epam.cdp.core.entity.BookingRequestEnum;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ua.com.taxi.dao.BookingDao;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.ClientDetails;
import ua.com.taxi.util.XstreamSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ua.com.taxi.entity.Booking.BookingStatus;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = Logger.getLogger(BookingServiceImpl.class);

    private static final int MAX_BOOKING_SELECT_LIMIT = 100;
    private static final String REST_URL_PARAMETERS =
            "?orderId={orderId}&bookingRequestId={bookingRequestId}&action={action}&reason={reason}";

    private final XstreamSerializer xstreamSerializer = new XstreamSerializer();
    @Autowired
    BookingDao bookingDao;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${router.rest.url}")
    private String ROUTER_REST_URL;
    @Value("${jms.fail.queue.name}")
    private String JMS_FAIL_QUEUE_NAME;
    @Value("${local.assign.expiry.time}")
    private Integer ASSIGN_EXPIRY_TIME;

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

    //TODO: check for null
    @Override
    public Booking findFreeBooking() {
        //TODO: fix this stub method
        final List<Booking> bookings = bookingDao.findBookingByStatus(BookingStatus.NEW, MAX_BOOKING_SELECT_LIMIT);
        bookings.addAll(bookingDao.findBookingByStatus(BookingStatus.REVOKED, MAX_BOOKING_SELECT_LIMIT));

        final int size = bookings.size();
        if (size > 0) {
            final int index = new Random().nextInt(size);
            return bookings.get(index);
        } else {
            return null;
        }
    }

    /**
     * Assign booking to an dispatcher to prevent possibility to process booking concurrently
     * @param bookingId
     * @return updated booking object from DB
     *  null - action can't be executed
     *
     */
    @Override
    public Booking assignBooking(final Long bookingId) {
        final Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.ASSIGNED)){
            LOG.warn(String.format("Current booking state can't be changed from %s to ASSIGNED", booking.getStatus()));
            //return booking;
            //TODO: null means that we can't execute assign action. It should be fixed to not null object
            return null;
        }

        booking.setStatus(BookingStatus.ASSIGNED);
        booking.setAssignToExpiryTime(new DateTime().plusMinutes(ASSIGN_EXPIRY_TIME));
        return bookingDao.update(booking);
    }

    @Override
    public Booking revokeBooking(final Long bookingId) {
        final Booking booking = bookingDao.find(bookingId);

        if (!canChangeStatusTo(booking, BookingStatus.REVOKED)) {
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to REVOKED");
            return booking;
        }

        booking.setStatus(BookingStatus.REVOKED);
        booking.setAssignToExpiryTime(null);
        return bookingDao.update(booking);
    }

    @Override
    public Booking acceptBooking(final Long bookingId) {
        final Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.ACCEPTED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to ACCEPTED");
            return booking;
        }

        final Map<String, String> mapVariables = createRequestParams(booking, BookingRequestEnum.Action.ACCEPT, "");
        final String response = executeRestRequest(mapVariables);
        final ClientDetails clientDetails = parseResponse(response, ClientDetails.class);

        if (clientDetails != null) {
            booking.setClient(clientDetails);
            booking.setStatus(BookingStatus.ACCEPTED);
            bookingDao.update(booking);
        }

        return booking;
    }

    @Override
    public Booking rejectBooking(final Long bookingId) {
        final Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.REJECTED)){
            LOG.warn(String.format("Current booking state can't be changed from %s  to REJECTED", booking.getStatus()));
            return booking;
        }

        final Map<String, String> mapVariables = createRequestParams(booking, BookingRequestEnum.Action.REJECT, "");
        final String response = executeRestRequest(mapVariables);
        final BookingRequestEnum.Status status = parseResponse(response, BookingRequestEnum.Status.class);

        switch (status) {
            case REJECTED:
                booking.setStatus(BookingStatus.REJECTED);
                break;
            case EXPIRED:
                booking.setStatus(BookingStatus.EXPIRED);
                break;
            default:
                LOG.error(String.format("Unexpected response status [%s] for reject request", status));
                return null;
        }

        bookingDao.update(booking);
        return booking;
    }

    @Override
    public Booking refuseBooking(final Long bookingId, final String reason) {
        final Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.REFUSED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to REFUSED");
            return booking;
        }

        final Map<String, String> mapVariables = createRequestParams(booking, BookingRequestEnum.Action.REFUSE, reason);
        final String response = executeRestRequest(mapVariables);
        final BookingRequestEnum.Status status = parseResponse(response, BookingRequestEnum.Status.class);

        if(status == BookingRequestEnum.Status.REFUSED) {
            booking.setStatus(BookingStatus.REFUSED);
        } else {
            LOG.error(String.format("Unexpected response status [%s] for refuse request", status));
            return null;
        }

        bookingDao.update(booking);
        return booking;
    }

    @Override
    public Boolean sendTextMessageToFailQueue(final String xmlBookingRequestMessage) {
        jmsTemplate.send(JMS_FAIL_QUEUE_NAME, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(xmlBookingRequestMessage);
            }
        });
        LOG.warn("Booking request  was sent to fail queue\n" + xmlBookingRequestMessage);
        return true;
    }

    @Override
    public Long countActualBookings() {
        final long newCount = bookingDao.countBookingByStatus(BookingStatus.NEW);
        final long unassignedCount = bookingDao.countBookingByStatus(BookingStatus.REVOKED);
        return newCount + unassignedCount;
    }

    @Override
    public List<Booking> findExpiredBookings() {
        return bookingDao.findExpiredBookings();
    }

    @Override
    public List<Booking> findBookingWithExpiredAssignedStatus() {
        return bookingDao.findBookingWithExpiredAssignedStatus();
    }

    @Override
    public List<Booking> findBookings(final int limit) {
        return bookingDao.findBooking(limit);
    }

    @Override
    public List<Booking> findBookingByStatus(final BookingStatus status, final int limit) {
        return bookingDao.findBookingByStatus(status, limit);
    }

    private Boolean isNotExpired(final Booking booking) {
        return booking.getBookingRequest().getExpiryTime().isAfter(new DateTime());
    }

    private Boolean canChangeStatusTo(final Booking booking, final Booking.BookingStatus newStatus) {
        final Booking.BookingStatus currentStatus = booking.getStatus();
        switch (newStatus) {
        case NEW:
            return isNotExpired(booking);
        case ASSIGNED:
            return (currentStatus == BookingStatus.NEW || currentStatus == BookingStatus.REVOKED) && isNotExpired(
                    booking);

        case REVOKED:
            return currentStatus == BookingStatus.ASSIGNED && isNotExpired(booking);

        case ACCEPTED:
            return currentStatus == BookingStatus.ASSIGNED && isNotExpired(booking);

        case REJECTED:
            return currentStatus == BookingStatus.ASSIGNED && isNotExpired(booking);

        case REFUSED:
            return currentStatus == BookingStatus.ACCEPTED || currentStatus == BookingStatus.REJECTED;

        case EXPIRED:
            return !isNotExpired(booking);

        default:
            LOG.error("Unexpected new status");
            throw new RuntimeException("Unexpected new status");
        }
    }

    private Map<String, String> createRequestParams(final Booking booking, final BookingRequestEnum.Action action,
            final String reason) {
        final Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("orderId", booking.getBookingRequest().getOrderId());
        mapVariables.put("bookingRequestId", booking.getBookingRequest().getBookingRequestId().toString());
        mapVariables.put("action", action.toString());
        mapVariables.put("reason", reason);
        return mapVariables;
    }

    private String executeRestRequest(final Map<String, String> mapVariables) {
        final String response;
        try {
            response = restTemplate.getForObject(ROUTER_REST_URL + REST_URL_PARAMETERS, String.class, mapVariables);
        } catch (final HttpClientErrorException ex) {
            LOG.error("Can't execute GET request to router module", ex);
            //TODO: custom exception
            throw new RuntimeException(ex);
        }
        return response;
    }

    private <T> T parseResponse(final String response, final Class<T> deserializedClass) {
        try {
            return xstreamSerializer.deserialize(response, deserializedClass);
        } catch (final Exception ex) {
            LOG.error("Can't parse response:\n" + response, ex);
            //            TODO: custom exception
            throw new RuntimeException(ex);
        }
    }

}
