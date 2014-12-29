package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;

/**
 * @author Geniy00
 */
public interface ReservationRequestHandler {

    ReservationResponse handleIndicativeRequest(ReservationRequest reservationRequest);

    ReservationResponse handleOrderingRequest(ReservationRequest reservationRequest);

}
