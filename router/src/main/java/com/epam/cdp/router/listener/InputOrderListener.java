package com.epam.cdp.router.listener;

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
public class InputOrderListener implements MessageListener {

    public static final Logger LOG = Logger.getLogger(InputOrderListener.class);

    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        Serializable reservationRequestObject = null;
        try {
            reservationRequestObject = objectMessage.getObject();
        } catch (JMSException e) {
            LOG.error("Can't get object from received message");
            e.printStackTrace();
        }

        if (reservationRequestObject  instanceof ReservationRequest) {
            ReservationRequest reservationRequest = (ReservationRequest) reservationRequestObject;
            Order order = orderService.createAndSaveNewOrder(reservationRequest);
            LOG.info("Order with id: " + order.getId() + " was created successfully");
        } else {
            LOG.error("Unknown JMS message type. It must be of ReservationRequest type");
            throw new IllegalArgumentException("Unknown JMS message type");
        }

    }
}
