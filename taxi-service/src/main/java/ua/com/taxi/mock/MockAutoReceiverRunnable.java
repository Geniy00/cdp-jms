package ua.com.taxi.mock;

import org.apache.log4j.Logger;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockAutoReceiverRunnable implements Runnable {

    private static final Logger LOG = Logger.getLogger(MockAutoReceiverRunnable.class);

    private final BookingService bookingService;

    private final int delayBetweenReceiving;
    private final int rejectEveryNthOrder;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

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

            final Booking booking = bookingService.findFreeBooking();
            if (booking == null) {
                continue;
            }

            currentNumberOfOrder++;
            //Accept booking
            final Long bookingId = booking.getId();
            final String orderId = booking.getBookingRequest().getOrderId();
            if (shouldBeAccepted(currentNumberOfOrder)) {
                //TODO: do we need the status "assign"?
                bookingService.assignBooking(bookingId);
                bookingService.acceptBooking(bookingId);
                LOG.info(String.format("Booking[id:%d] for orderId[%s] was automatically accepted", bookingId,
                        orderId));
            } else {
                currentNumberOfOrder = 0;
                bookingService.assignBooking(bookingId);
                bookingService.rejectBooking(bookingId);
                LOG.info(String.format("Booking[id:%d] for orderId[%s] was automatically rejected", bookingId,
                        orderId));
            }
        } while (enabled.get());
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
        return currentNumberOfOrder < rejectEveryNthOrder;
    }
}
