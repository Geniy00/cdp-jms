package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.Order;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderDao {

    Order find(String id);

    List<Order> findAllOrders(int limit);

    List<Order> findOrderByStatus(Order.OrderStatus status, int limit);

}
