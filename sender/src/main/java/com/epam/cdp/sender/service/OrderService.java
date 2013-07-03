package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.Order;

public interface OrderService {
	
	void sendOrder(Order order);
	
}