package com.epam.cdp.receiver.bean;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.entity.Order;

/**
 * This class presents concurrent blocking list
 * 
 * @author Geniy
 *
 */
@Component
public class OrderBlockingListImpl implements OrderBlockingList {
	
	private static final Logger LOG = Logger.getLogger(OrderBlockingListImpl.class);
	
	private static final int TRY_LOCK_TIME = 5;//seconds
	private LinkedList<Order> orderList;
	private int capacity = 5;
	private Lock lock;
	private Condition isFull;
	
	public OrderBlockingListImpl(int capacity) {
		this.capacity = capacity;
		orderList = new LinkedList<Order>();	
		lock = new ReentrantLock();
		isFull = lock.newCondition();
	}
	
	public void add(Order order){
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			while(orderList.size() == capacity){
				isFull.await();
			}
			orderList.addLast(order);	
		} catch (InterruptedException e) {
			LOG.error("Can't add order to list because it took too long time");
		} finally {
			lock.unlock();
		}
	}
	
	public Order peekOrder(){
		Order order = null;
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
		
			if(orderList.size() == 0){
				lock.unlock();
				return null;
			}
			
			order = orderList.pollFirst();
			orderList.addLast(order);
			
		} catch (InterruptedException e) {
			LOG.error("Can't peek order from list because it took too long time");
		} finally {
			lock.unlock();
		}
		return order;
	}
	
	public Order remove(String id){
		Order order = new Order();
		order.setId(id);
		
		Order fullOrder = null;
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			
			//remove order from list
			int index = orderList.indexOf(order);
			if(index < 0){
				LOG.warn("Can't to find order with id: " + id);
				return null;
			}		
			fullOrder = orderList.remove(index);
			isFull.signal();
		} catch (InterruptedException e) {
			LOG.error("Can't remove order from the list because it took too long time");
		} finally {
			lock.unlock();
		}
		
		return fullOrder;
	}

	public int size(){
		int size = 0;
		try{
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			size = orderList.size();
		} catch (InterruptedException e) {
			LOG.error("Can't get size of the list because it took too long time");
		} finally {
			lock.unlock();
		}
		return size;
	}
	
	public int getCapacity() {
		return capacity;
	}
}
