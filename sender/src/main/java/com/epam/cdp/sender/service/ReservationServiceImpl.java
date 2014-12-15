package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

//TODO: check if Apache Camel can be used
@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    @Override
    public void sendReservationRequest(final ReservationRequest reservationRequest) {
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(reservationRequest);
            }
        });
        final String deliveryTime = reservationRequest.getDeliveryTime().toString("dd-MMM-yyyy, HH:mm");
        LOG.info(String.format("New ReservationRequest[deliveryTime: %s, from %s to %s] was sent", deliveryTime,
                reservationRequest.getStartPosition(), reservationRequest.getFinishPosition()));
    }

}
