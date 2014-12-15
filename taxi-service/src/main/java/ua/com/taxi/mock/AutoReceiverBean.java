package ua.com.taxi.mock;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.service.BookingService;

@ThreadSafe
@Component
public class AutoReceiverBean {

    private static final Logger LOG = Logger.getLogger(AutoReceiverBean.class);

    @Autowired
    private BookingService bookingService;

    @CheckForNull
    private MockAutoReceiverRunnable mockAutoReceiverRunnable;

    public synchronized void start(final int delay, final int rejectEveryNthOrder) {
        if (mockAutoReceiverRunnable == null) {
            mockAutoReceiverRunnable = new MockAutoReceiverRunnable(bookingService, delay, rejectEveryNthOrder);
            new Thread(mockAutoReceiverRunnable).start();
            LOG.info("Auto receiver bean was started");
        } else {
            LOG.info("AutoReceiverBean is already started.");
        }
    }

    public synchronized void stop() {
        if (mockAutoReceiverRunnable != null) {
            mockAutoReceiverRunnable.disable();
            mockAutoReceiverRunnable = null;
            LOG.info("Auto receiver bean was stopped");
        }
    }

    public synchronized boolean isEnabled() {
        return mockAutoReceiverRunnable != null;
    }
}
