package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.router.dao.CustomerDao;
import com.epam.cdp.router.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transaction;
import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    CustomerDao customerDao;
    
    @Autowired
    OrderDao orderDao;

    @Override
    public Order createAndSaveNewOrder(ReservationRequest reservationRequest) {
        Customer customer = new Customer(reservationRequest.getCustomerPhone(), reservationRequest.getCustomerName());
        customer = customerDao.saveOrUpdate(customer);

        String id = UUID.randomUUID().toString();
        Order order = new Order(id, customer, reservationRequest);
        return orderDao.saveOrUpdate(order);
    }

    @Override
    public void updateOrder(Order order){
        orderDao.saveOrUpdate(order);
    }

    @Override
    public List<Order> findAllByOrderStatus(Order.OrderStatus status) {
        return orderDao.findAllByOrderStatus(status);
    }
}
