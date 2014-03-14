package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;

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

    Boolean isOrderActual(Long id);

    Customer acceptOrder(Long id);

    Boolean rejectOrder(Long id);

    Boolean refuseOrder(Long id, String reason);
}
