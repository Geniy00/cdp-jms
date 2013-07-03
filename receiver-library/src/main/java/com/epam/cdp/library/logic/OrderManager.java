package com.epam.cdp.library.logic;


import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.core.entity.Report.HistoryItem;
import com.epam.cdp.core.entity.Report.ReportStatus;
import com.epam.cdp.library.bean.OrderBlockingList;
import com.epam.cdp.library.service.ReportService;

public final class OrderManager {

	private static final Logger LOG = Logger.getLogger(OrderManager.class);

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
	ReportService reportService;
	
	private String taxiId;
	
	public OrderManager(String taxiId) {
		this.taxiId = taxiId;
		orderBlockingList = context.getBean(OrderBlockingList.class);
		reportService = context.getBean(ReportService.class);
	}
	
	/**
	 * get Order with hidden customer information
	 * @return
	 */
	public Order peekOrder(){
		Order order = orderBlockingList.peekOrder();
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
	
	public Order acceptOrder(String id){
		Order order = orderBlockingList.remove(id);
		
		//generate report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.ACCEPTED, "order accepted", taxiId));
		
		reportService.sendReport(report);
		
		return order;
	}
	
	public Order rejectOrder(String id){
		Order order = orderBlockingList.remove(id);
		
		//generate report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REJECTED, "order rejected", taxiId));
		
		reportService.sendReport(report);
		
		//!!!send to queue again
		
		//return clonned order
		Order clonedOrder = new Order(
				order.getId(), 
				new Customer("hidden", "hidden"), 
				order.getStartPosition(), 
				order.getFinishPosition(), 
				order.getDateTime(), 
				order.getOrderType());
		return clonedOrder;
	}
	
	public void refuseOrder(Order order, String reason){
		
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REFUSED, reason, taxiId));
		
		reportService.sendReport(report);
		
	}
	
	public int getQueueSize(){
		return orderBlockingList.size();
	}

	public String getTaxiId() {
		return taxiId;
	}
	
	
}
