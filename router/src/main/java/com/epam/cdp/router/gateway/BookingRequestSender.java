package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.router.service.CostService;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.TaxiDispatcherSelector;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

/**
 * @author Geniy00
 */
@Component
public class BookingRequestSender {

    public static final Logger LOG = Logger.getLogger(BookingRequestSender.class);
    public static final int BOOKING_REQUEST_MINUTES_EXPIRATION = 5;                 // 5 mins
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    OrderService orderService;
    @Autowired
    TaxiDispatcherSelector taxiDispatcherSelector;
    @Autowired
    CostService costService;

    public void execute() {
        List<Order> newOrders = orderService.findAllByOrderStatus(Order.OrderStatus.NEW);

        for (Order order : newOrders) {
            BookingRequest bookingRequest = createBookingRequest(order);
            TaxiDispatcher taxiDispatcher = taxiDispatcherSelector.selectTaxiDispatcher(order);

            sendBookingRequest(taxiDispatcher.getJmsQueue(), bookingRequest);

            order.addBookingRequest(bookingRequest);
            order.setOrderStatus(Order.OrderStatus.SENT);
            orderService.updateOrder(order);
        }

        if (newOrders.size() > 0) {
            LOG.info(newOrders.size() + " orders were sent successfully.");
        }
    }

    private BookingRequest createBookingRequest(Order order) {
        Double payment = costService.calculateTaxiServicePayment(order.getReservationRequest());

        DateTime expiryTime = new DateTime().plusMinutes(BOOKING_REQUEST_MINUTES_EXPIRATION);
        DateTime deliveryTime = order.getReservationRequest().getDeliveryTime();
        if (expiryTime.isAfter(deliveryTime)) expiryTime = deliveryTime;

        return new BookingRequest(order, payment, expiryTime);
    }

    private void sendBookingRequest(String destination, final BookingRequest bookingRequest) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(bookingRequest);
            }
        });
    }

}
