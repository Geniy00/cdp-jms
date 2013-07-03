package com.epam.cdp.receiver.logic;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.core.entity.Report.HistoryItem;
import com.epam.cdp.core.entity.Report.ReportStatus;
import com.epam.cdp.receiver.bean.OrderBlockingList;
import com.epam.cdp.receiver.service.ReportService;

public class OrderManager {

	private static ApplicationContext context = load();
	
	private static ApplicationContext load(){
		ApplicationContext ctx = null;
		try{
			ctx = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
		} catch (BeansException e) {
			LOG.error("Can't load spring-context.xml file, from OrderManager class");
		}
		return ctx;
	}
	
	private static final Logger LOG = Logger.getLogger(OrderManager.class);
	
	@Autowired
	OrderBlockingList orderBlockingList;
	
	@Autowired
	ReportService reportService;
	
	private String taxiId;
	
	
	public OrderManager(String taxiId) {
		this.taxiId = taxiId;
	}
	
	//TODO: remove it
	@PostConstruct
	private void setup(){
		//load context
	}

	/**
	 * get Order with hidden customer information
	 * @return
	 */
	public Order peekOrder(){
		Order order = orderBlockingList.peekOrder();
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
		
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.ACCEPTED, "ok", taxiId));
		
		reportService.sendReport(report);
		
		//send report
		return order;
	}
	
	public void rejectOrder(String id, String reason){
		Order order = orderBlockingList.remove(id);
		
		//send report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REJECTED, reason, taxiId));
		
		reportService.sendReport(report);
	}
	
	public void refuseOrder(String id, String reason){
		Order order = orderBlockingList.remove(id);
		
		//send report
		Report report = new Report(order);
		report.addHistoryItem(new HistoryItem(ReportStatus.REFUSED, reason, taxiId));
		
		reportService.sendReport(report);
	}

	public String getTaxiId() {
		return taxiId;
	}
	
	
}
