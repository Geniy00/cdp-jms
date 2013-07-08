package com.epam.cdp.library.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;


@Service
public class JmsServiceImpl implements JmsService {

	private static final Logger LOG = Logger.getLogger(JmsServiceImpl.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${jms.reportQueue.name}")
	private String REPORT_DESTINATION;
	@Value("${jms.orderQueue.name}")
	private String ORDER_DESTINATION;
	@Value("${jms.errorQueue.name}")
	private String ERROR_DESTINATION;
	
	public void sendReport(final Report report) {
		jmsTemplate.send(REPORT_DESTINATION, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(report);
			}
		});
		LOG.debug("Report with id: " + report.getId() + " was sent");
	}

	public void sendOrderBack(final Order order) {
		jmsTemplate.send(ORDER_DESTINATION, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(order);
			}
		});
		LOG.info("Order with id: " + order.getId() + " was returned back");
	}

	public void sendFailureReport(final Report report) {
		jmsTemplate.send(ERROR_DESTINATION, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(report);
			}
		});
		LOG.info("Report with id: " + report.getId() + " was sent to the error queue");
		
	}

}
