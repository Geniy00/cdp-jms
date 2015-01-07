package ua.com.taxi.mock;

import com.epam.cdp.core.entity.TsException;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MockAutoReceiverRunnable implements Runnable {

    private static final Logger LOG = Logger.getLogger(MockAutoReceiverRunnable.class);

    private final BookingService bookingService;

    private final int delayBetweenReceiving;
    private final int rejectEveryNthOrder;
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final AtomicInteger safeFailureCount = new AtomicInteger(10);

    public MockAutoReceiverRunnable(final BookingService bookingService, final int delayBetweenReceiving,
            final int rejectEveryNthOrder) {
        this.bookingService = bookingService;
        this.delayBetweenReceiving = delayBetweenReceiving;
        this.rejectEveryNthOrder = rejectEveryNthOrder;
    }

    @Override
    public void run() {
        enabled.set(true);
        int currentNumberOfOrder = 0;

        do {
            try {
                Thread.sleep(delayBetweenReceiving);
            } catch (final InterruptedException ex) {
                LOG.error(ex.getMessage(), ex);
            }

            final Optional<Booking> bookingOptional = bookingService.findFreeBooking();
            if (!bookingOptional.isPresent()) {
                continue;
            }
            final Booking booking = bookingOptional.get();

            currentNumberOfOrder++;
            //Accept booking
            final Long bookingId = booking.getId();
            final String orderId = booking.getBookingRequest().getOrderId();
            try {
                processOrder(orderId, bookingId, currentNumberOfOrder);
            } catch (final TsException ex) {
                LOG.warn("Can't process an order", ex);
                if(safeFailureCount.decrementAndGet() <= 0) {
                    enabled.set(false);
                    LOG.warn("Auto receiver mock is stopped");
                }
            }
        } while (enabled.get());
    }

    private void processOrder(final String orderId, final Long bookingId, final int currentNumberOfOrder)
            throws TsException {
        bookingService.assignBooking(bookingId);
        LOG.debug(String.format("Booking[%s] is assigned", bookingId));
        final String action;
        if (shouldBeAccepted(currentNumberOfOrder)) {
            bookingService.acceptBooking(bookingId);
            action = "accepted";
        } else {
            bookingService.rejectBooking(bookingId);
            action = "rejected";
        }
        LOG.info(String.format("Booking[id:%d] for orderId[%s] was %s automatically", bookingId,
                orderId, action));
    }

    public int getDelayBetweenReceiving() {
        return delayBetweenReceiving;
    }

    public int getRejectEveryNthOrder() {
        return rejectEveryNthOrder;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void disable() {
        enabled.set(false);
    }

    private boolean shouldBeAccepted(final int currentNumberOfOrder) {
        return currentNumberOfOrder % rejectEveryNthOrder != 0;
    }
}
