package com.epam.cdp.library.bean;

import com.epam.cdp.core.entity.Order;

public interface OrderBlockingList {

	public void add(Order order);
	
	public Order peekOrder();

	public Order remove(String id);

	public int size();

	public int getCapacity();

}