package com.epam.cdp.sender.bean;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.sender.service.ReservationService;
import com.epam.cdp.sender.util.ReservationRequestGenerator;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class ScheduledReservationRequestSender {

    private static final Logger LOG = Logger.getLogger(ScheduledReservationRequestSender.class);
    private static final int INITIAL_DELAY = 0;
    private static final int CORE_THREAD_POOL_SIZE = 1;

    private final ReservationService reservationService;

    private final AtomicLong messageCount = new AtomicLong();
    private final AtomicLong delay = new AtomicLong();

    private final ScheduledExecutorService service;
    private final Runnable runnableSender;

    private final Lock lock = new ReentrantLock();
    @GuardedBy("lock")
    @CheckForNull
    private ScheduledFuture scheduledFuture;

    public ScheduledReservationRequestSender(ReservationService reservationService) {
        this.reservationService = reservationService;
        service = Executors.newScheduledThreadPool(CORE_THREAD_POOL_SIZE);
        runnableSender = createRunnableSender();
    }

    public void startSending(final long delay) {
        lock.lock();
        try {
            if (isSenderScheduled())
                return;

            this.delay.set(delay);
            this.messageCount.set(0);

            scheduledFuture = service.scheduleWithFixedDelay(runnableSender, INITIAL_DELAY, this.delay.get(),
                    TimeUnit.MILLISECONDS);
            LOG.info("Scheduled reservation request sender was started");
        } finally {
            lock.unlock();
        }
    }

    public void stopSending() {
        lock.lock();
        try {
            if (!isSenderScheduled())
                return;

            assert scheduledFuture != null;
            scheduledFuture.cancel(false);

            LOG.info("Scheduled reservation request sender was stopped");
        } finally {
            lock.unlock();
        }
    }

    public boolean isSending() {
        return isSenderScheduled();
    }

    public long getDelay() {
        return delay.get();
    }

    public long getMessageCount() {
        return messageCount.get();
    }

    private Runnable createRunnableSender() {
        return new Runnable() {
            public void run() {
                ReservationRequest reservationRequest = ReservationRequestGenerator.generateRandomReservationRequest();
                reservationService.sendReservationRequest(reservationRequest);
                messageCount.incrementAndGet();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Reservation request was sent[id:%s]", reservationRequest.getId()));
                }
            }
        };
    }

    private boolean isSenderScheduled() {
        return scheduledFuture != null;
    }

}
