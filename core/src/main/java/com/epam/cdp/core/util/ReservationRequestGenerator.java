package com.epam.cdp.core.util;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.VehicleType;
import org.joda.time.DateTime;

import java.util.Random;
import java.util.UUID;

public class ReservationRequestGenerator {

    //private static final String NUMBERS_AND_SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";
    private static Random random = new Random();

    private static String generate(int size, String symbols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return sb.toString();
    }

    public static ReservationRequest generateRandomReservationRequest() {
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setId(UUID.randomUUID().toString());
        reservationRequest.setCustomerName(generate(7, SYMBOLS));
        reservationRequest.setCustomerPhone("+38063" + generate(7, NUMBERS));
        reservationRequest.setStartPosition(random.nextInt(100));
        reservationRequest.setFinishPosition(random.nextInt(100));
        reservationRequest.setDeliveryTime(new DateTime().plusMinutes(20));
        VehicleType vehicleType = VehicleType.values()[random.nextInt(VehicleType.values().length)];
        reservationRequest.setVehicleType(vehicleType);
        return reservationRequest;
    }

}
