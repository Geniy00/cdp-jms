package com.epam.cdp.core.util;

import java.util.Random;
import java.util.UUID;

import org.joda.time.DateTime;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Order.OrderType;

public class OrderGenerator {
	
	//private static final String NUMBERS_AND_SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static final String NUMBERS = "0123456789";
	private static final String SYMBOLS = "abcdefghijklmnopqrstuvwxyz";
		
	private static Random random = new Random();
	
	private static String generate(int size, String symbols){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(symbols.charAt(random.nextInt(symbols.length())));
		}
		return sb.toString();
	}
	
	public static Order generateRandomOrder(){
		Order order = new Order();
		order.setId(UUID.randomUUID().toString());
		order.setCustomer(generateRandomCustomer());
		order.setStartPosition(random.nextInt(100));
		order.setFinishPosition(random.nextInt(100));
		order.setDateTime(new DateTime().plusDays(1));
		OrderType orderType =  OrderType.values()[random.nextInt(OrderType.values().length)];
		order.setOrderType(orderType);
		order.calculatePrice();
		return order;
	}
	
	public static Customer generateRandomCustomer(){
		Customer customer = new Customer();
		customer.setPhone("+38063" + generate(7, NUMBERS));
		customer.setName(generate(7, SYMBOLS));
		return customer;
	}
}
