package ua.com.taxi.mock;

import org.apache.log4j.Logger;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

public class MockAutoReceiver implements Runnable {

    private static final Logger LOG = Logger.getLogger(MockAutoReceiver.class);

    /**
     * Delay between  receiving orders
     */
    private int delay;

    /**
     * Nth booking will be rejected automatically
     */
    private int rejectEveryNthOrder;

    private boolean enabled;

    BookingService bookingService;

    public MockAutoReceiver(BookingService bookingService, int delay, int rejectEveryNthOrder) {
        super();
        this.bookingService = bookingService;
        this.delay = delay;
        this.rejectEveryNthOrder = rejectEveryNthOrder;
        enabled = false;
    }

    @Override
    public void run() {
        enabled = true;
        int currentNumberOfOrder = 0;

        do {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Booking booking = bookingService.findFreeBooking();
            if(booking == null){
                continue;
            }

            currentNumberOfOrder++;
            //Accept booking
            if (currentNumberOfOrder < rejectEveryNthOrder) {
                bookingService.assignBooking(booking.getId());
                bookingService.acceptBooking(booking.getId());
                LOG.info("Booking[id:" + booking.getId() + "] for orderId[" + booking.getBookingRequest().getOrderId()
                        + "] was automatically accepted");
            } else {
                //Reject booking
                currentNumberOfOrder = 0;
                bookingService.assignBooking(booking.getId());
                bookingService.rejectBooking(booking.getId());
                LOG.info("Booking[id:" + booking.getId() + "] for orderId[" + booking.getBookingRequest().getOrderId()
                        + "] was automatically rejected");
            }

        } while (enabled);

    }

    public int getDelay() {
        return delay;
    }

    public int getRejectEveryNthOrder() {
        return rejectEveryNthOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void disable() {
        enabled = false;
    }
}
