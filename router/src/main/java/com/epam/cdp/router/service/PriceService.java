package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.ReservationRequest;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

    private static final double TAX_PERCENTS = 0.15;

    private static final double minPrice = 5;

    public static Double calculateClientPrice(ReservationRequest reservationRequest) {
        final double length = calculateLength(reservationRequest);
        final double price = roundToTwoDigits(length);
        if (price < minPrice) {
            return price + minPrice;
        } else {
            return price;
        }
    }

    public static Double calculateTaxiServicePayment(ReservationRequest reservationRequest) {
        final double res = calculateClientPrice(reservationRequest) * TAX_PERCENTS;
        return roundToTwoDigits(res);
    }

    private static double calculateLength(ReservationRequest reservationRequest) {
        return Math.abs((double) reservationRequest.getStartPosition() - reservationRequest.getFinishPosition());
    }

    private static double roundToTwoDigits(final double res) {
        return Math.round(res * 100) / 100.0;
    }
}
