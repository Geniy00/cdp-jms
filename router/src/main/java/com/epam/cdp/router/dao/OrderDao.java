package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.Order;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderDao {

    Order saveOrUpdate(Order order);

    Order find(String id);

    BookingRequest updateBookingRequest(BookingRequest bookingRequest);

    BookingRequest findBookingRequest(Long id);

    List<Order> findExpiredOrders();

    List<BookingRequest> findExpiredBookingRequests();

    void delete(Order order);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);

    Order loadOrderEager(String id);
}
