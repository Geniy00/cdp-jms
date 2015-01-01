package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.BookingResponse;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.router.handler.BookingRequestHandler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @author Geniy00
 */
@Component
public class PeriodicTasks {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookingRequestHandler bookingRequestHandler;

    public void sendBookingRequests() {
        List<Order> ordersToProcess = fetchOrdersToProcess();
        removeExpiredOrders(ordersToProcess);

        for (final Order order : ordersToProcess) {
            bookingRequestHandler.sendBookingRequest(order);
        }
    }

    public void terminateExpiredBookingRequests() {
        orderService.terminateExpiredBookingRequests();
    }

    public void terminateExpiredOrders() {
        orderService.terminateExpiredOrders();
    }

    private void removeExpiredOrders(final List<Order> ordersToProcess) {
        final Iterator<Order> iterator = ordersToProcess.iterator();
        while (iterator.hasNext()) {
            final Order order = iterator.next();

            final DateTime orderDeliveryTime = order.getReservationRequest().getDeliveryTime();
            if (isExpired(orderDeliveryTime)) {
                final Order expiredOrder = orderService.loadOrderEager(order.getId());

                //attach default expired bookingResponse
                for (BookingRequest bookingRequest : expiredOrder.getBookingRequests()) {
                    if (bookingRequest.getBookingResponse() == null) {
                        final BookingResponse expiredBookingResponse = new BookingResponse(bookingRequest,
                                BookingRequestEnum.Status.EXPIRED, TimeService.getCurrentTimestamp());
                        bookingRequest.applyBookingResponse(expiredBookingResponse);
                    }
                }

                expiredOrder.setOrderStatus(Order.OrderStatus.EXPIRED);
                orderService.update(expiredOrder);
                iterator.remove();
            }
        }
    }

    private boolean isExpired(DateTime orderDeliveryTime) {
        return TimeService.getCurrentTimestamp().isAfter(orderDeliveryTime);
    }

    private List<Order> fetchOrdersToProcess() {
        List<Order> newOrders = orderService.findAllByOrderStatus(Order.OrderStatus.NEW);
        newOrders.addAll(orderService.findAllByOrderStatus(Order.OrderStatus.DECLINED));
        return newOrders;
    }

}
