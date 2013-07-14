package com.epam.cdp.library.listener;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.HistoryItem;
import com.epam.cdp.core.entity.HistoryItem.ReportStatus;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.library.bean.OrderBlockingList;
import com.epam.cdp.library.logic.OrderManager;
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
			Report report = new Report(order);
			HistoryItem historyItem = new HistoryItem(
					ReportStatus.FAILURE, "The order can't pass validation", 
					OrderManager.getTaxiId());
			report.addHistoryItem(historyItem);
			jmsService.sendFailureReport(report);
			return;
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
				|| order.getDateTime().isBefore(currentTime.minusMinutes(10))
				|| order.getDateTime().isAfter(currentTime.plusDays(30))
				|| order.getPrice() == null
				|| order.getPrice() <= 0){
			return false;
		}
		return true;
	}
	
}
