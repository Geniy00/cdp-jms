package ua.com.taxi.gateway;

import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.core.xml.BookingRequestMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import ua.com.taxi.dao.BookingDao;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.BookingRequestDetails;
import ua.com.taxi.service.BookingService;
import ua.com.taxi.util.XstreamSerializer;

import javax.jms.*;

@Component
public class BookingRequestGateway implements MessageListener {

    private static final Logger LOG = Logger.getLogger(BookingRequestGateway.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.fail.queue.name}")
    private String JMS_FAIL_QUEUE_NAME;

    private final XstreamSerializer xstreamSerializer = new XstreamSerializer();

    @Autowired
    BookingDao bookingDao;

    @Autowired
    BookingService bookingService;

    public void onMessage(final Message message) {
        final TextMessage textMessage = (TextMessage) message;
        final String xmlMessage;
        try {
            xmlMessage = textMessage.getText();
        } catch (final JMSException ex) {
            LOG.error("Can't get text from received message", ex);
            return;
        }

        BookingRequestMessage bookingRequestMessage = null;
        try {
            bookingRequestMessage = deserializeBookingRequestMessage(xmlMessage);
            if (bookingRequestMessage == null) {
                throw new TsException(TsException.Reason.DESERIALIZATION_FAILURE, xmlMessage,
                        BookingRequestMessage.class.getSimpleName());
            }
        } catch (final TsException ex) {
            LOG.error(ex);
            sendTextMessageToFailQueue(xmlMessage);
        }

        final BookingRequestDetails bookingRequestDetails = new BookingRequestDetails(bookingRequestMessage);
        final Booking booking = new Booking();
        booking.setBookingRequest(bookingRequestDetails);

        bookingService.saveOrUpdate(booking);
        LOG.info(String.format("New bookingRequestMessage[id: %s] is received.",
                booking.getBookingRequest().getOrderId()));
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
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

    private BookingRequestMessage deserializeBookingRequestMessage(final String xmlMessage) throws TsException {
        try {
            return xstreamSerializer.deserialize(xmlMessage, BookingRequestMessage.class);
        } catch (final Exception ex) {
            LOG.error("Can't deserialize BookingRequestMessage.", ex);
            sendTextMessageToFailQueue(xmlMessage);
            throw new TsException(ex, TsException.Reason.RESPONSE_PARSING_FAILURE, xmlMessage);
        }
    }

}
