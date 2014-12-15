package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.router.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

//TODO: move this package to gateway and fix spring context
@Component
public class ReservationRequestListener implements MessageListener {

    public static final Logger LOG = Logger.getLogger(ReservationRequestListener.class);

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(final Message message) {
        final Serializable reservationRequestObject = extractReservationRequestObject(message);

        if (reservationRequestObject instanceof ReservationRequest) {
            final ReservationRequest reservationRequest = (ReservationRequest) reservationRequestObject;
            final Order order = orderService.createAndSaveNewOrder(reservationRequest);
            LOG.info(String.format("Order[id: %s] was created successfully", order.getId()));
        } else {
            LOG.error("Unknown JMS message type. It must be of ReservationRequest type");
            //TODO: custom exception
            throw new IllegalArgumentException("Unknown JMS message type");
        }

    }

    private Serializable extractReservationRequestObject(final Message message) {
        final ObjectMessage objectMessage = (ObjectMessage) message;
        final Serializable reservationRequestObject;
        try {
            reservationRequestObject = objectMessage.getObject();
        } catch (final JMSException ex) {
            final String failMessage = "Can't get object from received message";
            LOG.error(message, ex);
            //TODO: look at custom checked exceptions
            throw new RuntimeException(failMessage, ex);
        }
        return reservationRequestObject;
    }
}
