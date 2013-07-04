package com.epam.cdp.library.bean;

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
	
	private static final int CAPACITY = 5;
	private static final int TRY_LOCK_TIMEOUT = 5;//seconds
	private LinkedList<Order> orderList;
	private Lock lock;
	private Condition isFull;
	
	public OrderBlockingListImpl() {
		orderList = new LinkedList<Order>();	
		lock = new ReentrantLock();
		isFull = lock.newCondition();
	}
	
	public void add(Order order){
		try {
			if(lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.SECONDS)){
				try {
					while(orderList.size() == CAPACITY){
						isFull.await();
					}
					orderList.addLast(order);	
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while adding new order");
			e.printStackTrace();
		}
		
	}
	
	public Order peek(){
		Order order = null;
		try {
			if(lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.SECONDS)){
				try{
					if(orderList.size() == 0){
						return null;
					}
					
					order = orderList.pollFirst();
					orderList.addLast(order);
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
		
			
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while peeking an order");
		} 
		return order;
	}
	
	public Order remove(String id){
		Order order = new Order();
		order.setId(id);
		
		Order fullOrder = null;
		try {
			if(lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.SECONDS)){
				try{
					//remove order from list
					int index = orderList.indexOf(order);
					if(index < 0){
						LOG.warn("Can't to find order with id: " + id);
						return null;
					}		
					fullOrder = orderList.remove(index);
					isFull.signal();
					
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
			
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while removing an order");
		} 
		
		return fullOrder;
	}

	public int size(){
		int size = 0;
		try{
			if(lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.SECONDS)){
				try{
					size = orderList.size();
				} finally {
					lock.unlock();
				}
			} else {
				LOG.error("Can't lock the list");
			}
			
		} catch (InterruptedException e) {
			LOG.error("InterruptedException was thrown while getting size an order");
		} 
		return size;
	}
	
	public int getCapacity() {
		return CAPACITY;
	}
}
