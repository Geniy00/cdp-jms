package com.epam.cdp.library.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.library.bean.OrderBlockingList;

@Component
public class OrderListener {

	public static final Logger LOG = Logger.getLogger(OrderListener.class);
	
	@Autowired
	OrderBlockingList orderBlockingList;
	
	public void onMessage(Order order){
		LOG.info("Order with id: " + order.getId() + " received");
		
		if(isCorrect(order) == false){
			LOG.warn("The order with id: " + order.getId() + " is uncorrect");
			throw new RuntimeException();
		}
		
		orderBlockingList.add(order);
		LOG.info("Order with id: " + order.getId() + " was sent to the queue");
	}
	
	protected boolean isCorrect(Order order){
		return true;
	}
	
}
