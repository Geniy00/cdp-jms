package ua.com.taxi.gateway;

import com.epam.cdp.core.xml.BookingRequestMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.dao.BookingDao;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.BookingRequestDetails;
import ua.com.taxi.service.BookingService;
import ua.com.taxi.util.XstreamSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class BookingRequestListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(BookingRequestListener.class);

    private final XstreamSerializer xstreamSerializer = new XstreamSerializer();

    @Autowired
    BookingDao bookingDao;

    @Autowired
    BookingService bookingService;

    public void onMessage(final Message message) {
        final TextMessage textMessage = (TextMessage) message;
        String xmlMessage = null;
        BookingRequestMessage bookingRequestMessage;
        try {
            xmlMessage = textMessage.getText();
            bookingRequestMessage = xstreamSerializer.deserialize(xmlMessage, BookingRequestMessage.class);
        } catch (final JMSException ex) {
            LOG.error("Can't get xml from received message", ex);
            return;
        } catch (final Exception ex) {
            LOG.error("Can't deserialize TextMessage.", ex);
            bookingService.sendTextMessageToFailQueue(xmlMessage);
            return;
        }

        if (!isCorrect(bookingRequestMessage)) {
            bookingService.sendTextMessageToFailQueue(xmlMessage);
            return;
        }

        final BookingRequestDetails bookingRequestDetails = new BookingRequestDetails(bookingRequestMessage);
        final Booking booking = new Booking();
        booking.setBookingRequest(bookingRequestDetails);

        bookingService.saveOrUpdate(booking);
        LOG.info(String.format("New bookingRequestMessage[id: %s] is received.",
                booking.getBookingRequest().getOrderId()));
    }

    protected boolean isCorrect(BookingRequestMessage bookingRequestMessage) {
        return bookingRequestMessage != null;
    }

}
