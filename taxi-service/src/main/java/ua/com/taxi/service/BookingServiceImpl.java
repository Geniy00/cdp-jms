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

import static ua.com.taxi.entity.Booking.BookingStatus;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    public static final Logger LOG = Logger.getLogger(BookingServiceImpl.class);
    private static final int MAX_BOOKING_COUNT_BATCH = 100;     //Max count of bookings that can be processed for one DB request

    @Value("${router.rest.url}")
    private String ROUTER_REST_URL;

    private String REST_URL_PARAMETERS = "?orderId={orderId}&bookingRequestId={bookingRequestId}&action={action}&reason={reason}";

    private Integer ASSIGN_EXPIRY_TIME = 3;         //3 mins

    private XstreamSerializer xstreamSerializer = new XstreamSerializer();

    @Autowired
    BookingDao bookingDao;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    RestTemplate restTemplate;

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
    public Booking findFreeBooking() {
        //TODO: fix this stub method
        List<Booking> bookings = bookingDao.findBookingByStatus(BookingStatus.NEW, MAX_BOOKING_COUNT_BATCH);
        bookings.addAll(bookingDao.findBookingByStatus(BookingStatus.UNASSIGNED, MAX_BOOKING_COUNT_BATCH));

        int size = bookings.size();
        if (size > 0) {
            int index = (int) (Math.random() * size);
            return bookings.get(index);
        } else {
            return null;
        }
    }

    private Boolean isNotExpired(Booking booking){
        return booking.getBookingRequest().getExpiryTime().isAfter(new DateTime());
    }

    private Boolean canChangeStatusTo(Booking booking, Booking.BookingStatus newStatus) {
        Booking.BookingStatus currentStatus = booking.getStatus();
        switch (newStatus) {
            case NEW:    
                return isNotExpired(booking);
            case ASSIGNED:
                return (currentStatus == BookingStatus.NEW
                        || currentStatus == BookingStatus.UNASSIGNED)
                        && isNotExpired(booking);

            case UNASSIGNED:
                return currentStatus == BookingStatus.ASSIGNED
                        && isNotExpired(booking);

            case ACCEPTED:
                return currentStatus == BookingStatus.ASSIGNED
                        && isNotExpired(booking);

            case REJECTED:
                return currentStatus == BookingStatus.ASSIGNED
                        && isNotExpired(booking);

            case REFUSED:
                return currentStatus == BookingStatus.ACCEPTED
                        || currentStatus == BookingStatus.REJECTED;

            case EXPIRED:
                return !isNotExpired(booking);

            default:
                LOG.error("Unexpected new status");
                throw new RuntimeException("Unexpected new status");
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
    public Booking assignBooking(Long bookingId) {
        Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.ASSIGNED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to ASSIGNED");
            //return booking;
            //TODO: null means that we can't execute assign action. It should be fixed to not null object
            return null;
        }
        
        DateTime assignToExpiryTime = new DateTime().plusMinutes(ASSIGN_EXPIRY_TIME);
        booking.setStatus(BookingStatus.ASSIGNED);
        booking.setAssignToExpiryTime(assignToExpiryTime);
        return bookingDao.update(booking);
    }

    @Override
    public Booking unassignBooking(Long bookingId) {
        Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.UNASSIGNED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to UNASSIGNED");
            return booking;
        }

        booking.setStatus(BookingStatus.UNASSIGNED);
        booking.setAssignToExpiryTime(null);
        return bookingDao.update(booking);
    }

    @Override
    public Booking acceptBooking(Long bookingId) {
        Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.ACCEPTED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to ACCEPTED");
            return booking;
        }

        Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("orderId", booking.getBookingRequest().getOrderId());
        mapVariables.put("bookingRequestId", booking.getBookingRequest().getBookingRequestId().toString());
        mapVariables.put("action", BookingRequestEnum.Action.ACCEPT.toString());
        mapVariables.put("reason", "");
        String response = restTemplate.getForObject(
                ROUTER_REST_URL + REST_URL_PARAMETERS,
                String.class,
                mapVariables);

        ClientDetails clientDetails = null;
        try {
            clientDetails = xstreamSerializer.deserialize(response, ClientDetails.class);
        } catch (Exception e) {
            LOG.error("Can't parse response:\n" + response);
        }

        if (clientDetails != null) {
            booking.setClient(clientDetails);
            booking.setStatus(BookingStatus.ACCEPTED);
            bookingDao.update(booking);
        }

        return booking;
    }

    @Override
    public Booking rejectBooking(Long bookingId) {
        Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.REJECTED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + "  to REJECTED");
            return booking;
        }

        Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("orderId", booking.getBookingRequest().getOrderId());
        mapVariables.put("bookingRequestId", booking.getBookingRequest().getBookingRequestId().toString());
        mapVariables.put("action", BookingRequestEnum.Action.REJECT.toString());
        mapVariables.put("reason", "");
        String response = restTemplate.getForObject(
                ROUTER_REST_URL + REST_URL_PARAMETERS,
                String.class,
                mapVariables);

        BookingRequestEnum.Status status = null;
        try {
            status = xstreamSerializer.deserialize(response, BookingRequestEnum.Status.class);
        } catch (Exception e) {
            LOG.error("Can't parse response:\n" + response);
        }

        switch (status) {
            case REJECTED:
                booking.setStatus(BookingStatus.REJECTED);
                break;
            case EXPIRED:
                booking.setStatus(BookingStatus.EXPIRED);
                break;
            default:
                LOG.error("Unexpected response status [" + status + "] for reject request");
                return null;
        }

        bookingDao.update(booking);
        return booking;
    }

    @Override
    public Booking refuseBooking(Long bookingId, String reason) {
        Booking booking = bookingDao.find(bookingId);

        if(!canChangeStatusTo(booking, BookingStatus.REFUSED)){
            LOG.warn("Current booking state can't be changed from " + booking.getStatus() + " to REFUSED");
            return booking;
        }

        Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("orderId", booking.getBookingRequest().getOrderId());
        mapVariables.put("bookingRequestId", booking.getBookingRequest().getBookingRequestId().toString());
        mapVariables.put("action", BookingRequestEnum.Action.REFUSE.toString());
        mapVariables.put("reason", reason);
        String response = restTemplate.getForObject(
                ROUTER_REST_URL + REST_URL_PARAMETERS,
                String.class,
                mapVariables);

        BookingRequestEnum.Status status = null;
        try {
            status = xstreamSerializer.deserialize(response, BookingRequestEnum.Status.class);
        } catch (Exception e) {
            LOG.error("Can't parse response:\n" + response);
        }

        if(status == BookingRequestEnum.Status.REFUSED) {
            booking.setStatus(BookingStatus.REFUSED);
        } else {
            LOG.error("Unexpected response status [" + status + "] for refuse request");
            return null;
        }

        bookingDao.update(booking);
        return booking;
    }

    @Override
    public Boolean sendTextMessageToFailQueue(final String xmlBookingRequestMessage) {
        LOG.error("JMS destination isn't set");
        jmsTemplate.send("", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(xmlBookingRequestMessage);
            }
        });
        LOG.warn("Booking request " + xmlBookingRequestMessage + " was sent to fail queue");
        return true;
    }

    @Override
    public Long countActualBookings() {
        long newCount = bookingDao.countBookingByStatus(BookingStatus.NEW);
        long unassignedCount = bookingDao.countBookingByStatus(BookingStatus.UNASSIGNED);
        return newCount + unassignedCount;
    }

    @Override
    public List<Booking> findExpiredBookings() {
        return bookingDao.findExpiredBookings();
    }

    @Override
    public List<Booking> findBookings(int limit) {
        return bookingDao.findBooking(limit);
    }

    @Override
    public List<Booking> findBookingByStatus(BookingStatus status, int limit) {
        return bookingDao.findBookingByStatus(status, limit);
    }
}
