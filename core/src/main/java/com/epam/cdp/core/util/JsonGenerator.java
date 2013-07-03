package com.epam.cdp.core.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.epam.cdp.core.entity.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class is used for generating json presentation of Order class
 * and for parsing json files to array of Orders
 * @author Geniy
 *
 */
public class JsonGenerator {
	
	private static final int ORDER_COUNT = 10;
	private static final String PATH_TO_FILE = "c:\\orders.txt";
	
	public static void main(String[] args) throws IOException {
						
		Order[] orders = new Order[ORDER_COUNT];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = OrderGenerator.generateRandomOrder();
		}
		
		Gson gson = new GsonBuilder()
			.setDateFormat("dd/MMM/yyyy HH:mm:ss")
			.registerTypeAdapter(DateTime.class, new DateTimeSerialization())
			.setPrettyPrinting().create();

		String json = gson.toJson(orders);
		FileUtils.writeStringToFile(new File(PATH_TO_FILE), json);
		
		System.out.println("Json file was created!");
	}
	
	public Order[] parseJson(String jsonString){
		Gson gson = new GsonBuilder()
			.setDateFormat("dd/MMM/yyyy HH:mm:ss")
			.registerTypeAdapter(DateTime.class, new DateTimeSerialization())
			.setPrettyPrinting().create();
		
		Order[] orders = gson.fromJson(jsonString, Order[].class);
		return orders;
	}
	
}

class DateTimeSerialization implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
	
	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MMM/yyyy HH:mm:ss");
	
	public JsonElement serialize(DateTime src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toString(formatter));
	}

	public DateTime deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new DateTime(formatter.parseDateTime(
				json.getAsJsonPrimitive().getAsString()));
	}
}
