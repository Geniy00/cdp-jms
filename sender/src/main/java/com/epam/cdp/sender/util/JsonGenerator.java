package com.epam.cdp.sender.util;

import com.epam.cdp.core.entity.ReservationRequest;
import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

/**
 * @author Geniy00
 */
public class JsonGenerator {

    public ReservationRequest[] parseJson(String jsonString) {
        Gson gson = new GsonBuilder().setDateFormat("dd/MMM/yyyy HH:mm:ss").registerTypeAdapter(DateTime.class,
                new DateTimeSerialization()).setPrettyPrinting().create();

        ReservationRequest[] reservationRequests = gson.fromJson(jsonString, ReservationRequest[].class);
        return reservationRequests;
    }

    public static class DateTimeSerialization implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MMM/yyyy HH:mm:ss");

        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString(formatter));
        }

        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new DateTime(formatter.parseDateTime(json.getAsJsonPrimitive().getAsString()));
        }
    }
}
