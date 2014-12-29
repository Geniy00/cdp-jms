package com.epam.cdp.sender.gateway;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.sender.bean.RequestStorageBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

/**
 * @author Geniy00
 */
@Component
public class SenderGateway implements MessageListener {

    private static final Logger LOG = Logger.getLogger(SenderGateway.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RequestStorageBean storageBean;

    @Autowired
    private Queue requestQueue;

    @Autowired
    private Queue responseQueue;


    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    public void send(final ReservationRequest reservationRequest) {
        jmsTemplate.send(requestQueue, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                final ObjectMessage message = session.createObjectMessage(reservationRequest);
                message.setJMSReplyTo(responseQueue);
                return message;
            }
        });
    }

    @Override
    public void onMessage(final Message message) {
        try {
            final ReservationResponse reservationResponse = extractObject(message, ReservationResponse.class);
            storageBean.update(reservationResponse);
        } catch (final TsException ex) {
            LOG.error(ex);
        }
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
