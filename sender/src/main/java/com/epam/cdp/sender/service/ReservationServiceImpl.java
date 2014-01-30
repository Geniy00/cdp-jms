package com.epam.cdp.sender.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.epam.cdp.core.entity.ReservationRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.epam.cdp.core.entity.Order;

@Service
public class ReservationServiceImpl implements ReservationService {

	private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Override
	public void sendReservationRequest(final ReservationRequest reservationRequest) {
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(reservationRequest);
			}
		});
		LOG.info("Order with id: " + reservationRequest.getId() + " was sent");
	}

}
