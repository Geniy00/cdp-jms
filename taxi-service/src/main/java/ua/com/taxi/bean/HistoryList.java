package ua.com.taxi.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;

import ua.com.taxi.entity.Report;

@Component
public class HistoryList {

	private static final int CAPACITY = 100;
	private static final int TRYLOCK_INTERVAL = 5;
	private static final Logger LOG = Logger.getLogger(HistoryList.class);
	
	private LinkedList<Report> reportList;
	private Lock lock;
	
	public HistoryList() {
		reportList = new LinkedList<Report>();
		lock = new ReentrantLock();
	}
	
	public void add(Report report){
		try {
			if(lock.tryLock(TRYLOCK_INTERVAL, TimeUnit.SECONDS)){
				try{
					if(reportList.size() == 100){
						reportList.pollLast();
					}
					
					reportList.addFirst(report);
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while adding new report to the history");
			e.printStackTrace();
		} 
	}
	
	public List<Report> getReportHistory() {
		List<Report> list = new ArrayList<Report>(CAPACITY);
		
		try {
			if(lock.tryLock(TRYLOCK_INTERVAL, TimeUnit.SECONDS)){
				try{
					list.addAll(reportList);
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while getting history");
			e.printStackTrace();
		} 
		
		return list;
	}
	
	public Order getOrderById(String id){
		try {
			if(lock.tryLock(TRYLOCK_INTERVAL, TimeUnit.SECONDS)){
				try{
					for(Report report : reportList){
						if(report.getId().equals(id)){
							return report.getOrder();
						}
					}
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while getting order by id");
			e.printStackTrace();
		} 
		return null;
	}
}
