package com.epam.cdp.library.listener;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.library.bean.OrderBlockingList;
import com.epam.cdp.library.service.JmsService;

@Component
public class OrderListener {

	public static final Logger LOG = Logger.getLogger(OrderListener.class);
	
	@Autowired
	OrderBlockingList orderBlockingList;
	
	@Autowired
	JmsService jmsService;
	
	public void onMessage(Order order){
		LOG.info("Order with id: " + order.getId() + " received");
		
		if(isCorrect(order) == false){
			LOG.warn("The order with id: " + order.getId() + " is uncorrect");
			jmsService.sendFailureOrder(order);
		}
		
		orderBlockingList.add(order);
		LOG.info("Order with id: " + order.getId() + " was sent to the queue");
	}
	
	protected boolean isCorrect(Order order){
		DateTime currentTime = new DateTime();
		
		if(order.getId() == null || order.getId().isEmpty()
				|| order.getCustomer() == null 
				|| order.getCustomer().getPhone() == null || order.getCustomer().getPhone().isEmpty()
				|| order.getStartPosition() == null
				|| order.getFinishPosition() == null
				|| order.getDateTime().isAfter(currentTime.minusMinutes(10))
				|| order.getDateTime().isAfter(currentTime.plusDays(30))
				|| order.getPrice() == null){
			return false;
		}
		return true;
	}
	
}
