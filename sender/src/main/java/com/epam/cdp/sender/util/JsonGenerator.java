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
        final Gson gson = new GsonBuilder().setDateFormat("dd/MMM/yyyy HH:mm:ss").registerTypeAdapter(DateTime.class,
                new DateTimeSerialization()).setPrettyPrinting().create();

        return gson.fromJson(jsonString, ReservationRequest[].class);
    }

    public static class DateTimeSerialization implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd/MMM/yyyy HH:mm:ss");

        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString(DATE_TIME_FORMATTER));
        }

        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new DateTime(DATE_TIME_FORMATTER.parseDateTime(json.getAsJsonPrimitive().getAsString()));
        }
    }
}
