package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.sender.controller.TaxiReservationController;
import edu.umd.cs.findbugs.annotations.CheckForNull;

public interface ReservationService {

    void sendToJms(ReservationRequest reservationRequest);

    Long generateRequestId();

    Long priceRequest(final ReservationRequest reservationRequest,
            TaxiReservationController taxiReservationController);

    void orderRequest(Long requestId);

    @CheckForNull
    ReservationRequest getRequestById(Long requestId);

    @CheckForNull
    ReservationResponse getResponseById(Long requestId);

}