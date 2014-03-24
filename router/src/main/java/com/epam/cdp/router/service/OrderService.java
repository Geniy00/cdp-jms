package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order createAndSaveNewOrder(ReservationRequest reservationRequest);

    Order update(Order order);

    void delete(Order order);

    Order find(String id);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);

    void terminateExpiredOrders();

    void terminateExpiredBookingRequests();

    void updateOrder(Order order);

    BookingRequest updateBookingRequest(BookingRequest bookingRequest);

    Customer acceptOrder(String orderId, Long bookingRequestId);

    BookingRequestEnum.Status rejectOrder(String orderId, Long bookingRequestId);

    BookingRequestEnum.Status refuseOrder(String orderId, Long bookingRequestId, String reason);

    Order loadOrderEager(String id);

    void persistFailQueueMessage(FailQueueMessage failQueueMessage);
}
