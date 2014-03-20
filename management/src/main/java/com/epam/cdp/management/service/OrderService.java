package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.Order;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order find(String id);

    List<Order> findAllOrders(int limit);

    List<Order> findOrderByStatus(Order.OrderStatus status, int limit);

}
