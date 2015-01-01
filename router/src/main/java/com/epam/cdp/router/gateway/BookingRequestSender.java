package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.core.xml.BookingRequestMessage;
import com.epam.cdp.router.service.XmlSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @author Geniy00
 */
@Component
public class BookingRequestSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private XmlSerializer xmlSerializer;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    public void send(final TaxiDispatcher taxiDispatcher, final BookingRequest persistedBookingRequest) {
        final String xmlMessage = serializeBookingRequest(persistedBookingRequest);
        final String destination = taxiDispatcher.getJmsQueue();

        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                return session.createTextMessage(xmlMessage);
            }
        });
    }

    private String serializeBookingRequest(final BookingRequest persistedBookingRequest) {
        final BookingRequestMessage bookingRequestMessage = new BookingRequestMessage(persistedBookingRequest);
        return xmlSerializer.serialize(bookingRequestMessage);
    }

}
