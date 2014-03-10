package ua.com.taxi.listener;

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

    public static final Logger LOG = Logger.getLogger(BookingRequestListener.class);

    XstreamSerializer xstreamSerializer = new XstreamSerializer();

    @Autowired
    BookingDao bookingDao;

    @Autowired
    BookingService bookingService;

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        String xmlMessage = null;

        try {
            xmlMessage = textMessage.getText();
        } catch (JMSException e) {
            LOG.error("Can't get object from received message");
            e.printStackTrace();
        }

        BookingRequestMessage bookingRequestMessage = xstreamSerializer.deserialize(xmlMessage, BookingRequestMessage.class);

        if (!isCorrect(bookingRequestMessage)) {
            bookingService.sendTextMessageToFailQueue(xmlMessage);
            return;
        }

        BookingRequestDetails bookingRequestDetails = new BookingRequestDetails(bookingRequestMessage);
        Booking booking = new Booking();
        booking.setBookingRequest(bookingRequestDetails);

        bookingService.saveOrUpdate(booking);
        LOG.info("New booking request with id:" + booking.getBookingRequest().getBookingRequestId() + " received.");
    }

    protected boolean isCorrect(BookingRequestMessage bookingRequestMessage) {
        return true;
    }

}
