package com.epam.cdp.sender.bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.cdp.core.util.OrderGenerator;
import com.epam.cdp.sender.service.OrderService;

@Component
public class ScheduledOrderSender {

	private static final Logger LOG = Logger.getLogger(ScheduledOrderSender.class);
	
	@Autowired
	OrderService orderService;
	
	private Status status;
	private long messageCount;
	private long delay;

	private ScheduledExecutorService service;
	
	public enum Status {
		SENDING, STOPPED
	}
	
	public ScheduledOrderSender(){
		status = Status.STOPPED;
		delay = 5000;
	}
	
	public void startSending(long delay){
		this.delay = delay;
		if(status == Status.SENDING) return;
		
		messageCount = 0;
		
		service = Executors.newScheduledThreadPool(1);
		Runnable runnableSender = new Runnable() {
			public void run() {
				orderService.sendOrder(OrderGenerator.generateRandomOrder());
				messageCount++;
			}
		};
		service.scheduleWithFixedDelay(runnableSender, 0, this.delay, TimeUnit.MILLISECONDS);
		status = Status.SENDING;
		LOG.info("Scheduled order sender was started");
	}

	public void stopSending(){
		if(status == Status.STOPPED) return;
		
		service.shutdown();
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOG.warn("Stop message sending was broken. It took more than 10 seconds.");
			e.printStackTrace();
		}
		status = Status.STOPPED;
		LOG.info("Scheduled order sender was stopped");
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
}