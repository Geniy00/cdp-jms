package com.epam.cdp.sender.util;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.VehicleType;
import org.joda.time.DateTime;

import java.util.Random;

public class ReservationRequestGenerator {

    //private static final String NUMBERS_AND_SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_SET = "0123456789";
    private static final String SYMBOL_SET = "abcdefghijklmnopqrstuvwxyz";
    private static final Random random = new Random();

    private static final int MINUTES_TO_PROCESS_ORDER = 20;
    private static final int ROUTE_DISTANCE = 100;

    private static String generate(int size, String symbols) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return sb.toString();
    }

    public static ReservationRequest generateRandomReservationRequest() {
        final String customerName = generate(7, SYMBOL_SET);
        final String customerPhone = "+38063" + generate(7, NUMBER_SET);
        final int startPosition = random.nextInt(ROUTE_DISTANCE);
        final int finishPosition = random.nextInt(ROUTE_DISTANCE);
        final DateTime deliveryTime = new DateTime().plusMinutes(MINUTES_TO_PROCESS_ORDER);

        final int randomIndex = random.nextInt(VehicleType.values().length);
        final VehicleType vehicleType = VehicleType.values()[randomIndex];
        return new ReservationRequest(null, customerName, customerPhone, startPosition, finishPosition, deliveryTime,
                vehicleType);
    }

}
