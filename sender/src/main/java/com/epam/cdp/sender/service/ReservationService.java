package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;

public interface ReservationService {

    void sendReservationRequest(ReservationRequest reservationRequest);

}