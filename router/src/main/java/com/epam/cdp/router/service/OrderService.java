package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;

import java.util.List;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order createAndSaveNewOrder(Long requestId);

    Order update(Order order);

    ReservationRequest update(ReservationRequest reservationRequest);

    void delete(Order order);

    Order find(String id);

    List<Order> findAllByOrderStatus(Order.OrderStatus status);

    void terminateExpiredOrders();

    void terminateExpiredBookingRequests();

    void terminateExpiredPricedRequests();

    BookingRequest updateBookingRequest(BookingRequest bookingRequest);

    Customer getCustomerInfo(String orderId, Long bookingRequestId) throws TsException;

    BookingRequest.Status acceptOrder(String orderId, Long bookingRequestId) throws TsException;

    BookingRequest.Status rejectOrder(String orderId, Long bookingRequestId) throws TsException;

    BookingRequest.Status refuseOrder(String orderId, Long bookingRequestId, String reason) throws TsException;

    Order loadOrderEager(String id);

    void persistFailQueueMessage(FailQueueMessage failQueueMessage);
}
