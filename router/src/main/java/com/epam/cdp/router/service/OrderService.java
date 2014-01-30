package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;

/**
 * @author Geniy00
 */
public interface OrderService {

    Order createAndSaveNewOrder(ReservationRequest reservationRequest);

}
