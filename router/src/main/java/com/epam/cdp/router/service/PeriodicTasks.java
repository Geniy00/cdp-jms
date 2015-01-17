package com.epam.cdp.router.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Geniy00
 */
@Component
public class PeriodicTasks {

    @Autowired
    private OrderService orderService;

    public void terminateExpiredPricedReservationRequests(){
        orderService.terminateExpiredPricedRequests();
    }

    public void terminateExpiredBookingRequests() {
        orderService.terminateExpiredBookingRequests();
    }

    public void terminateExpiredOrders() {
        orderService.terminateExpiredOrders();
    }

}
