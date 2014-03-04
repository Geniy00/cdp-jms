package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Order;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderDao {

    Order saveOrUpdate(Order order);

    Order find(String id);

    void delete(Order order);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);
}
