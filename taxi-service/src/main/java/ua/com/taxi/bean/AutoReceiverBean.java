package ua.com.taxi.bean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.taxi.receiver.MockAutoReceiver;
import ua.com.taxi.service.OrderService;

@Component
public class AutoReceiverBean {

	private static final Logger LOG = Logger.getLogger(AutoReceiverBean.class);
	
	@Autowired
	OrderService orderService;
	
	MockAutoReceiver mockAutoReceiver;
	
	
	public void start(int delay, int rejectEveryNthOrder){
		mockAutoReceiver = new MockAutoReceiver(orderService);
		mockAutoReceiver.setDelay(delay);
		mockAutoReceiver.setRejectEveryNthOrder(rejectEveryNthOrder);
		LOG.info("Auto receiver bean was started");
	}
	
	public void stop(){
		mockAutoReceiver.stop();
		LOG.info("Auto receiver bean was stopped");
	}
	
	
}
