package ua.com.taxi.mock;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.service.BookingService;

@Component
public class AutoReceiverBean {

    private static final Logger LOG = Logger.getLogger(AutoReceiverBean.class);

    @Autowired
    BookingService bookingService;

    @CheckForNull
    MockAutoReceiverRunnable mockAutoReceiverRunnable;

    public void start(final int delay, final int rejectEveryNthOrder) {
        if (!isEnabled()) {
            LOG.info("AutoReceiverBean is already started.");
            return;
        }
        mockAutoReceiverRunnable.disable();
        mockAutoReceiverRunnable = new MockAutoReceiverRunnable(bookingService, delay, rejectEveryNthOrder);
        new Thread(mockAutoReceiverRunnable).start();
        LOG.info("Auto receiver bean was started");
    }

    public void stop() {
        if (isEnabled()) {
            mockAutoReceiverRunnable.disable();
            LOG.info("Auto receiver bean was stopped");
        }
    }

    public Boolean isEnabled() {
        return mockAutoReceiverRunnable != null;
    }
}
