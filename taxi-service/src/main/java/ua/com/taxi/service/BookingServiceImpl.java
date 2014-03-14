package ua.com.taxi.service;

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

/**
 * @author Geniy00
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    public static final Logger LOG = Logger.getLogger(BookingServiceImpl.class);

    @Value("${router.rest.url}")
    private String ROUTER_REST_URL;

    private String REST_URL_PARAMETERS = "?id={id}&action={action}&reason={reason}";

    private XstreamSerializer xstreamSerializer;

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
        List<Booking> bookings = bookingDao.findBookingByStatus(Booking.BookingStatus.NEW);
        bookings.addAll(bookingDao.findBookingByStatus(Booking.BookingStatus.UNASSIGNED));

        int size = bookings.size() - 1;
        if (size >= 0) {
            int index = (int) (Math.random() * size);
            return bookings.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void assignBooking(Booking booking, DateTime dateTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unassignBooking(Booking booking) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Booking acceptBooking(Long bookingId) {
        Booking booking = bookingDao.find(bookingId);

        Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("id", booking.getBookingRequest().getId().toString());
        mapVariables.put("action", "ACCEPT");
        mapVariables.put("reason", "");
        String response = restTemplate.getForObject(
                ROUTER_REST_URL + REST_URL_PARAMETERS,
                String.class,
                mapVariables);

        ClientDetails clientDetails = xstreamSerializer.deserialize(response, ClientDetails.class);
        booking.setClient(clientDetails);
        bookingDao.update(booking);
        return booking;
    }

    @Override
    public Boolean rejectBooking(Booking booking) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean refuseBooking(Booking booking, String reason) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        long newCount = bookingDao.countBookingByStatus(Booking.BookingStatus.NEW);
        long unassignedCount = bookingDao.countBookingByStatus(Booking.BookingStatus.UNASSIGNED);
        return newCount + unassignedCount;
    }
}
