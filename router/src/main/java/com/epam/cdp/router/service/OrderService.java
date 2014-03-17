package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order createAndSaveNewOrder(ReservationRequest reservationRequest);

    Order find(String id);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);

    void updateOrder(Order order);

    BookingRequest updateBookingRequest(BookingRequest bookingRequest);

    Customer acceptOrder(String orderId, Long bookingRequestId);

    BookingRequestEnum.Status rejectOrder(String orderId, Long bookingRequestId);

    BookingRequestEnum.Status refuseOrder(String orderId, Long bookingRequestId, String reason);

    Order loadOrderEager(String id);
}
