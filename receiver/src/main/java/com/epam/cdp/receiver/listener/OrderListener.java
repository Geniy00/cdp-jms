package com.epam.cdp.receiver.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.receiver.service.ReportService;

@Component
public class OrderListener {

	public static final Logger LOG = Logger.getLogger(OrderListener.class);
	
//	@Autowired
//	OrderService orderService;
	
	public void onMessage(Order order){
		LOG.info("Order with id: " + order.getId() + " received");
		
		
		
		
//		LOG.warn("Transaction for order with id: " + order.getId() + "will rollback");
//		if(true){
//			throw new RuntimeException("throw exception");
//		}
//		LOG.warn("Transaction wasn't rollback for order with id: " + order.getId());
//		LOG.info("message with id " + order.getId() + " was processed");
	}
	
	
	
}
