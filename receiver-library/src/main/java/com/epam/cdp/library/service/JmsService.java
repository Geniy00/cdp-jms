package com.epam.cdp.library.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;

/**
 * Interface for working with any jms queues and topics that needs for taxi service
 * @author Ievgen_Kuibida
 *
 */
public interface JmsService {
	
	/**
	 * Send report about an order
	 * @param report
	 */
	void sendReport(Report report);
	
	/**
	 * Send an order back to queue, if the order can't be processed
	 * @param order
	 */
	void sendOrderBack(Order order);
	
	/**
	 * Send an order that can't be processed, in the report with status FAILURE
	 * For example: the time is over, or order is broken
	 * @param order
	 */
	void sendFailureReport(Report report);
	
}