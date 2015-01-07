package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import edu.umd.cs.findbugs.annotations.CheckForNull;

public interface ReservationService {

    Long priceRequest(final ReservationRequest reservationRequest);

    void orderRequest(Long requestId);

    @CheckForNull
    ReservationRequest getRequestById(Long requestId);

    @CheckForNull
    ReservationResponse getResponseById(Long requestId);

}