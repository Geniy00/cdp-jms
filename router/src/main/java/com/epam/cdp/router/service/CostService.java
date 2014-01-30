package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import org.springframework.stereotype.Service;

@Service
public class CostService {

    public Double calculateOrderPrice(ReservationRequest reservationRequest){
        return Math.abs((double) reservationRequest.getStartPosition() - reservationRequest.getFinishPosition());
    }

    public Double calculateTaxiServicePayment(ReservationRequest reservationRequest){
        return calculateOrderPrice(reservationRequest) * 0.15;
    }
}
