package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.router.handler.ReservationRequestHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.Serializable;

@Service
public class ReservationRequestGateway implements MessageListener {

    private static final Logger LOG = Logger.getLogger(ReservationRequestGateway.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ReservationRequestHandler requestHandler;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    public void send(final String destination, final ReservationResponse reservationResponse) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("ReservationResponse >>> " + reservationResponse);
        }
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                return session.createObjectMessage(reservationResponse);
            }
        });
    }

    @Override
    public void onMessage(final Message message) {
        try {
            final ReservationRequest reservationRequest = extractObject(message, ReservationRequest.class);
            if (LOG.isDebugEnabled()) {
                LOG.debug("ReservationRequest <<< " + reservationRequest);
            }
            handleReservationRequest(reservationRequest);
        } catch (final TsException ex) {
            LOG.error(ex);
        }
    }

    private void handleReservationRequest(final ReservationRequest reservationRequest) {
        final ReservationResponse reservationResponse;
        if (reservationRequest.isIndicative()) {
            reservationResponse = requestHandler.handleIndicativeRequest(reservationRequest);
        } else {
            reservationResponse = requestHandler.handleOrderingRequest(reservationRequest);
        }
        final String destination = reservationRequest.getSourceSystem().getJmsResponseQueue();
        send(destination, reservationResponse);
    }

    private <T> T extractObject(final Message message, final Class<T> clazz) throws TsException {
        try {
            final Serializable serializableObject = ((ObjectMessage) message).getObject();
            if (serializableObject.getClass().equals(clazz)) {
                return clazz.cast(serializableObject);
            } else {
                throw new TsException(TsException.Reason.WRONG_INPUT_JMS_MESSAGE_TYPE, clazz.getSimpleName());
            }
        } catch (final JMSException ex) {
            final String failMessage = String.format("Can't get object from received %s message",
                    clazz.getSimpleName());
            throw new TsException(ex, TsException.Reason.CANNOT_PROCESS_JMS_MESSAGE, failMessage);
        }
    }
}
