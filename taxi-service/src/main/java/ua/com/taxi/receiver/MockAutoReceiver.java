//package ua.com.taxi.receiver;
//
//import com.epam.cdp.core.entity.Order;
//
//public class MockAutoReceiver implements Runnable {
//
//    /**
//     * Delay between  receiving orders
//     */
//    private int DELAY = 5000;
//
//    /**
//     * Nth booking will be rejected automatically
//     */
//    private int REJECT_EVERY_NTH_ORDER = 10;
//
//    private boolean stopFlag;
//
//    BookingService bookingService;
//
//    public MockAutoReceiver(BookingService bookingService) {
//        super();
//        this.bookingService = bookingService;
//        stopFlag = false;
//    }
//
//    @Override
//    public void run() {
//        int currentNumberOfOrder = 0;
//        do {
//            try {
//                Thread.sleep(DELAY);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Order booking = bookingService.peekBooking();
//            if (booking != null) {
//                currentNumberOfOrder++;
//                //Accept booking
//                if (currentNumberOfOrder < REJECT_EVERY_NTH_ORDER) {
//                    bookingService.acceptBooking(booking.getId());
//                } else {
//                    //Reject booking
//                    bookingService.rejectBooking(booking.getId());
//                }
//            }
//
//        } while (!stopFlag);
//
//    }
//
//    public int getDelay() {
//        return DELAY;
//    }
//
//    public void setDelay(int delay) {
//        this.DELAY = delay;
//    }
//
//    public int getRejectEveryNthOrder() {
//        return REJECT_EVERY_NTH_ORDER;
//    }
//
//    public void setRejectEveryNthOrder(int rejectEveryNthOrder) {
//        REJECT_EVERY_NTH_ORDER = rejectEveryNthOrder;
//    }
//
//    public void stop() {
//        stopFlag = true;
//    }
//}
