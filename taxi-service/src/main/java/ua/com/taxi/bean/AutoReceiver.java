package ua.com.taxi.bean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.taxi.receiver.Receiver;
import ua.com.taxi.service.OrderService;

@Component
public class AutoReceiver {

	private static final Logger LOG = Logger.getLogger(AutoReceiver.class);
	
	@Autowired
	OrderService orderService;
	
	Receiver receiver;
	
	
	public void start(int delay, int rejectEveryNthOrder){
		receiver = new Receiver(orderService);
		receiver.setDelay(delay);
		receiver.setRejectEveryNthOrder(rejectEveryNthOrder);
		LOG.info("Auto receiver was started");
	}
	
	public void stop(){
		receiver.stop();
		LOG.info("Auto receiver was stopped");
	}
	
	
}
