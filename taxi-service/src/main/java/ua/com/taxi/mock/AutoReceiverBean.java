package ua.com.taxi.mock;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.taxi.service.BookingService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
@Component
public class AutoReceiverBean {

    private static final Logger LOG = Logger.getLogger(AutoReceiverBean.class);

    @Autowired
    private BookingService bookingService;

    @GuardedBy("lock")
    @CheckForNull
    private MockAutoReceiverRunnable mockAutoReceiverRunnable;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void start(final int delay, final int rejectEveryNthOrder) {
        writeLock.lock();
        try {
            if (mockAutoReceiverRunnable == null) {
                mockAutoReceiverRunnable = new MockAutoReceiverRunnable(bookingService, delay, rejectEveryNthOrder);
                executorService.submit(mockAutoReceiverRunnable);
                LOG.info("Auto receiver bean was started");
            } else {
                LOG.info("AutoReceiverBean is already started.");
            }
        }finally {
            writeLock.unlock();
        }
    }

    public void stop() {
        writeLock.lock();
        try {
            if (mockAutoReceiverRunnable != null) {
                mockAutoReceiverRunnable.disable();
                mockAutoReceiverRunnable = null;
                LOG.info("Auto receiver bean was stopped");
            }
        } finally {
            writeLock.unlock();
        }
    }

    public boolean isEnabled() {
        readLock.lock();
        try {
            return mockAutoReceiverRunnable != null;
        } finally {
            readLock.unlock();
        }
    }

    public int getDelayBetweenReceiving() {
        readLock.lock();
        try {
            if (isEnabled()) {
                assert mockAutoReceiverRunnable != null;
                return mockAutoReceiverRunnable.getDelayBetweenReceiving();
            } else {
                return -1;
            }
        } finally {
            readLock.unlock();
        }
    }
    public int getRejectEveryNthOrder() {
        readLock.lock();
        try {
            if (isEnabled()) {
                assert mockAutoReceiverRunnable != null;
                return mockAutoReceiverRunnable.getRejectEveryNthOrder();
            } else {
                return -1;
            }
        } finally {
            readLock.unlock();
        }
    }
}
