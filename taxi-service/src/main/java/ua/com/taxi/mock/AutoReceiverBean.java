package ua.com.taxi.mock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.service.BookingService;

@Component
public class AutoReceiverBean {

    private static final Logger LOG = Logger.getLogger(AutoReceiverBean.class);

    @Autowired
    BookingService bookingService;

    MockAutoReceiver mockAutoReceiver;

    public void start(int delay, int rejectEveryNthOrder) {
        mockAutoReceiver = new MockAutoReceiver(bookingService, delay, rejectEveryNthOrder);
        new Thread(mockAutoReceiver).start();
        LOG.info("Auto receiver bean was started");
    }

    public void stop() {
        if (mockAutoReceiver == null) return;
        mockAutoReceiver.disable();
        LOG.info("Auto receiver bean was stopped");
    }

    public Boolean isEnabled() {
        if (mockAutoReceiver == null) return false;
        return mockAutoReceiver.isEnabled();
    }
}
