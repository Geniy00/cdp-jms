package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.management.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Geniy00
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public Order find(final String id) {
        return orderDao.find(id);
    }

    @Override
    public List<Order> findAllOrders(final int limit) {
        return orderDao.findAllOrders(limit);
    }

    @Override
    public List<Order> findOrderByStatus(final Order.OrderStatus status, final int limit) {
        return orderDao.findOrderByStatus(status, limit);
    }
}
