package com.epam.cdp.sender;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.sender.util.JsonGenerator;
import com.epam.cdp.sender.util.ReservationRequestGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

/**
 * This class is used for generating json presentation of ReservationRequest class
 * and for parsing json files to array of ReservationRequests
 *
 * @author Geniy00
 */
public class ReservationRequestFileGenerator {

    public static final String PATH_TO_FOLDER = "c:\\orders.txt";
    public static final int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws IOException {

        ReservationRequest[] reservationRequests = new ReservationRequest[MESSAGE_COUNT];
        for (int i = 0; i < reservationRequests.length; i++) {
            reservationRequests[i] = ReservationRequestGenerator.generateRandomReservationRequest();
        }

        Gson gson = new GsonBuilder().setDateFormat("dd/MMM/yyyy HH:mm:ss").registerTypeAdapter(DateTime.class,
                new JsonGenerator.DateTimeSerialization()).setPrettyPrinting().create();

        String json = gson.toJson(reservationRequests);
        FileUtils.writeStringToFile(new File(PATH_TO_FOLDER), json);

        System.out.println("Json file was created!");
    }
}
