package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.ReservationRequest;
import org.springframework.stereotype.Service;

@Service
public class CostService {

    private static final double TAX_PERCENTS = 0.15;

    public static Double calculateTaxiServicePayment(ReservationRequest reservationRequest) {
        final double res = calculateOrderPrice(reservationRequest) * TAX_PERCENTS;
        return Math.round(res * 100) / 100.0;
    }

    private static Double calculateOrderPrice(ReservationRequest reservationRequest) {
        return calculateLength(reservationRequest);
    }

    private static double calculateLength(ReservationRequest reservationRequest) {
        return Math.abs((double) reservationRequest.getStartPosition() - reservationRequest.getFinishPosition());
    }
}
