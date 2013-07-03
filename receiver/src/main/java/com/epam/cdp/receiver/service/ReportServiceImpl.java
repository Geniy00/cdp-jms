package com.epam.cdp.receiver.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.epam.cdp.core.entity.Report;


@Service
public class ReportServiceImpl implements ReportService {

	private static final Logger LOG = Logger.getLogger(ReportServiceImpl.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	public void sendReport(final Report report) {
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(report);
			}
		});
		LOG.info("Report with id: " + report.getId() + " was sent");
	}

}
