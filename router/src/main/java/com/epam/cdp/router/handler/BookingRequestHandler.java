package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.Order;

/**
 * @author Geniy00
 */
public interface BookingRequestHandler {

    void sendBookingRequest(final Order order);

}
