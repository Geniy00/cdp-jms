package com.epam.cdp.library.bean;

import com.epam.cdp.core.entity.Order;

public interface OrderBlockingList {

	/**
	 * Add order to list.
	 * If list is full(capacity == size), the thread will sleep till other thread remove some order
	 * @param order
	 */
	public void add(Order order);
	
	/**
	 * Retrieve order from the list without removing it
	 * @return
	 */
	public Order peek();

	/**
	 * remove order from the list
	 * @param id
	 * @return
	 */
	public Order remove(String id);

	/**
	 * Get current size of list(queue)
	 * @return
	 */
	public int size();

	public int getCapacity();

}