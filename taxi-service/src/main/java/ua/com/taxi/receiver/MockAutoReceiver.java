package ua.com.taxi.receiver;

import com.epam.cdp.core.entity.Order;
import ua.com.taxi.service.OrderService;

public class MockAutoReceiver implements Runnable {

    /**
     * Delay between  receiving orders
     */
    private int DELAY = 5000;

    /**
     * Nth order will be rejected automatically
     */
    private int REJECT_EVERY_NTH_ORDER = 10;

    private boolean stopFlag;

    OrderService orderService;

    public MockAutoReceiver(OrderService orderService) {
        super();
        this.orderService = orderService;
        stopFlag = false;
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

            Order order = orderService.peekOrder();
            if (order != null) {
                currentNumberOfOrder++;
                //Accept order
                if (currentNumberOfOrder < REJECT_EVERY_NTH_ORDER) {
                    orderService.acceptOrder(order.getId());
                } else {
                    //Reject order
                    orderService.rejectOrder(order.getId());
                }
            }

        } while (!stopFlag);

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

    public void stop() {
        stopFlag = true;
    }
}
