package com.epam.cdp.library.logic;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.HistoryItem;
import com.epam.cdp.core.entity.HistoryItem.ReportStatus;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.library.bean.OrderBlockingList;
import com.epam.cdp.library.service.JmsService;

public final class OrderManager {

	private static final Logger LOG = Logger.getLogger(OrderManager.class);

	/**
	 * Load library context when a classloader loads this class
	 */
	private static ApplicationContext context = load();
	private static ApplicationContext load(){
		ApplicationContext ctx = null;
		try{
			ctx = new ClassPathXmlApplicationContext("classpath:library-context.xml");
			LOG.info("The receiver-library context was initialized.");
		} catch (BeansException e) {
			LOG.error("Can't load the receiver-library context, from OrderManager class!");
		}
		return ctx;
	}

	OrderBlockingList orderBlockingList;	
	JmsService jmsService;
	
	/**
	 * Taxi service id, for distinguish taxi services
	 */
	private static String taxiId;
	
	public OrderManager(String taxiId) {
		OrderManager.taxiId = taxiId;
		//load beans from library spring context
		orderBlockingList = context.getBean(OrderBlockingList.class);
		jmsService = context.getBean(JmsService.class);
	}
	
	/**
	 * get Order with hidden customer information
	 * @return
	 */
	public Order peekOrder(){
		Order order = orderBlockingList.peek();
		if(order == null) return null;
		
		//hide customer information
		Order clonedOrder = new Order(
				order.getId(), 
				new Customer("hidden", "hidden"), 
				order.getStartPosition(), 
				order.getFinishPosition(), 
				order.getDateTime(), 
				order.getOrderType());
		
		return clonedOrder;
	}
	
	/**
	 * When taxi service accept an order, it will got customer information like phone number
	 * @param id
	 * @return an order with extra information about customer
	 */
	public Order acceptOrder(String id){
		Order order = orderBlockingList.remove(id);
		
		//generate report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.ACCEPTED, "order accepted", taxiId));
		
		jmsService.sendReport(report);
		
		return order;
	}
	
	public Order rejectOrder(String id){
		Order order = orderBlockingList.remove(id);
		
		//generate report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REJECTED, "order rejected", taxiId));
		
		//Send messages
		jmsService.sendReport(report);		
		jmsService.sendOrderBack(order);
		
		//return cloned order
		Order clonedOrder = new Order(
				order.getId(), 
				new Customer("hidden", "hidden"), 
				order.getStartPosition(), 
				order.getFinishPosition(), 
				order.getDateTime(), 
				order.getOrderType());
		return clonedOrder;
	}
	
	/**
	 * Order could be refused, when one was accepted
	 * For example: A client refused the order, when taxi-service called to him
	 * @param order
	 * @param reason
	 */
	public void refuseOrder(Order order, String reason){
		
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REFUSED, reason, taxiId));
		
		jmsService.sendReport(report);
		
	}
	
	public int getQueueSize(){
		return orderBlockingList.size();
	}

	public static String getTaxiId() {
		return taxiId;
	}
	
	
}
