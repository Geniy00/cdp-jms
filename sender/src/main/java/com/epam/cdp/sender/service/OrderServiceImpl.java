package com.epam.cdp.sender.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.epam.cdp.core.entity.Order;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	public void sendOrder(final Order order) {
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(order);
			}
		});
		LOG.info("Order with id: " + order.getId() + " was sent");
	}

}
