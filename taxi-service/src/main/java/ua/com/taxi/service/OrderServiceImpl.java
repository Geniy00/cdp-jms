package ua.com.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.taxi.bean.HistoryList;
import ua.com.taxi.entity.Report;
import ua.com.taxi.entity.Report.ReportStatus;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.library.logic.OrderManager;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderManager orderManager;
	
	@Autowired
	HistoryList historyList;

	public Order peekOrder(){
		return orderManager.peekOrder();
	}
	
	public Order acceptOrder(String id){
		Order order = orderManager.acceptOrder(id);
		
		Report report = new Report(order, ReportStatus.ACCEPTED, "order accepted");
		historyList.add(report);
		
		return order;
	}
	
	public Order rejectOrder(String id){
		Order order = orderManager.rejectOrder(id);
		
		Report report = new Report(order, ReportStatus.REJECTED, "order rejected");
		historyList.add(report);
		
		return order;
	}
	
	public Order refuseOrder(String id, String reason) {
		Order order = historyList.getOrderById(id);
		if(order == null) return null;
		
		orderManager.refuseOrder(order, reason);
		
		Report report = new Report(order, ReportStatus.REFUSED, reason);
		historyList.add(report);
		
		return order;
	}

	public int getQueueSize(){
		return orderManager.getQueueSize();
	}
}
