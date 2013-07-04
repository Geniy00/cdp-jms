package com.epam.cdp.management.listener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;

@Component
public class ErrorListener {

	public static final Logger LOG = Logger.getLogger(ErrorListener.class);
	
	public void onMessage(Order order){
		LOG.info("Error order with id " + order.getId() + " received");		
		//send to DB
	}
}
