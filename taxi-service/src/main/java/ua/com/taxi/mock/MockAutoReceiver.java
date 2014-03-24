package ua.com.taxi.mock;

import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

public class MockAutoReceiver implements Runnable {

    /**
     * Delay between  receiving orders
     */
    private int DELAY = 1000;

    /**
     * Nth booking will be rejected automatically
     */
    private int REJECT_EVERY_NTH_ORDER = 10;

    private boolean enabled;

    BookingService bookingService;

    public MockAutoReceiver(BookingService bookingService) {
        super();
        this.bookingService = bookingService;
        enabled = false;
    }

    @Override
    public void run() {
        int currentNumberOfOrder = 0;
        do {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Booking booking = bookingService.findFreeBooking();
            currentNumberOfOrder++;
            //Accept booking
            if (currentNumberOfOrder < REJECT_EVERY_NTH_ORDER) {
                bookingService.acceptBooking(booking.getId());
            } else {
                //Reject booking
                bookingService.rejectBooking(booking.getId());
            }

        } while (enabled);

    }

    public int getDelay() {
        return DELAY;
    }

    public void setDelay(int delay) {
        this.DELAY = delay;
    }

    public int getRejectEveryNthOrder() {
        return REJECT_EVERY_NTH_ORDER;
    }

    public void setRejectEveryNthOrder(int rejectEveryNthOrder) {
        REJECT_EVERY_NTH_ORDER = rejectEveryNthOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
