package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.core.xml.BookingRequestMessage;
import com.epam.cdp.router.service.CostService;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.TaxiDispatcherSelector;
import com.epam.cdp.router.service.XmlSerializer;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Iterator;
import java.util.List;

/**
 * @author Geniy00
 */
@Component
public class BookingRequestSender {

    public static final Logger LOG = Logger.getLogger(BookingRequestSender.class);
    public static final int BOOKING_REQUEST_MINUTES_EXPIRATION = 2;                 // 15 mins

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    OrderService orderService;

    @Autowired
    TaxiDispatcherSelector taxiDispatcherSelector;

    @Autowired
    CostService costService;

    @Autowired
    XmlSerializer xmlSerializer;

    public void execute() {
        List<Order> newOrders = orderService.findAllByOrderStatus(Order.OrderStatus.NEW);
        newOrders.addAll(orderService.findAllByOrderStatus(Order.OrderStatus.DECLINED));
        newOrders.addAll(orderService.findAllByOrderStatus(Order.OrderStatus.CANCELED));

        //check if order is expired
        Iterator<Order> iterator = newOrders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();

            //if expired
            if (new DateTime().isAfter(order.getReservationRequest().getDeliveryTime())) {
                order = orderService.loadOrderEager(order.getId());

                for (BookingRequest bookingRequest : order.getBookingRequests()) {
                    if (bookingRequest.getBookingResponse() == null) {
                        BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.EXPIRED);
                        bookingRequest.applyBookingResponse(bookingResponse);
                    }
                }

                order.setOrderStatus(Order.OrderStatus.EXPIRED);
                orderService.updateOrder(order);
                iterator.remove();
            }
        }

        for (Order order : newOrders) {

            //Select taxi dispatcher
            TaxiDispatcher taxiDispatcher = taxiDispatcherSelector.selectTaxiDispatcher(order);

            //Create xml message from BookingRequest attribute of Order
            BookingRequest bookingRequest = createBookingRequest(order, taxiDispatcher);
            bookingRequest.setOrder(order);
            bookingRequest = orderService.updateBookingRequest(bookingRequest);

            BookingRequestMessage bookingRequestMessage = new BookingRequestMessage(bookingRequest);
            String xmlMessage = xmlSerializer.serialize(bookingRequestMessage);

            sendBookingRequest(taxiDispatcher.getJmsQueue(), xmlMessage);

            order.applyBookingRequest(bookingRequest);
            orderService.updateOrder(order);
        }

        if (newOrders.size() > 0) {
            LOG.info(newOrders.size() + " BookingRequestMessages were sent successfully.");
        }
    }

    private BookingRequest createBookingRequest(Order order, TaxiDispatcher taxiDispatcher) {
        Double payment = costService.calculateTaxiServicePayment(order.getReservationRequest());

        DateTime expiryTime = new DateTime().plusMinutes(BOOKING_REQUEST_MINUTES_EXPIRATION);
        DateTime deliveryTime = order.getReservationRequest().getDeliveryTime();
        if (expiryTime.isAfter(deliveryTime)) expiryTime = deliveryTime;

        return new BookingRequest(order, taxiDispatcher, payment, expiryTime);
    }

    private void sendBookingRequest(String destination, final String xmlBookingRequestMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(xmlBookingRequestMessage);
            }
        });
    }

}
