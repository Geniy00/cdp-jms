package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order createAndSaveNewOrder(ReservationRequest reservationRequest);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);

    void updateOrder(Order order);
}
