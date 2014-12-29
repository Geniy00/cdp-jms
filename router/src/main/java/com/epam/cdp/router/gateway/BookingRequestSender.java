package com.epam.cdp.router.gateway;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.core.xml.BookingRequestMessage;
import com.epam.cdp.router.service.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${booking.request.expiration}")
    public int BOOKING_REQUEST_MINUTES_EXPIRATION;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    OrderService orderService;

    @Autowired
    TaxiDispatcherSelector taxiDispatcherSelector;

    @Autowired
    PriceService priceService;

    @Autowired
    XmlSerializer xmlSerializer;

    public void execute() {
        List<Order> ordersToProcess = fetchOrdersToProcess();
        //TODO: realize mechanism of manual sending of canceled orders
        //ordersToProcess.addAll(orderService.findAllByOrderStatus(Order.OrderStatus.CANCELED));

        //TODO: do we really need it
        removeExpiredOrders(ordersToProcess);

        for (Order order : ordersToProcess) {

            //Select taxi dispatcher
            final TaxiDispatcher taxiDispatcher = taxiDispatcherSelector.selectTaxiDispatcher(order);
            if (taxiDispatcher == null) {
                LOG.warn("Can't find any active taxi dispatcher. BookingRequest sending was postponed");
                return;
            }

            //Create xml message from BookingRequest attribute of Order
            final BookingRequest bookingRequest = createBookingRequest(order, taxiDispatcher);
            final BookingRequest persistedBookingRequest = orderService.updateBookingRequest(bookingRequest);

            sendBookingRequest(taxiDispatcher, persistedBookingRequest);

            order.applyBookingRequest(persistedBookingRequest);
            orderService.updateOrder(order);
        }

        if (ordersToProcess.size() > 0) {
            LOG.info(ordersToProcess.size() + " BookingRequestMessages were sent successfully.");
        }
    }

    private void removeExpiredOrders(final List<Order> ordersToProcess) {
        final Iterator<Order> iterator = ordersToProcess.iterator();
        while (iterator.hasNext()) {
            final Order order = iterator.next();

            final DateTime orderDeliveryTime = order.getReservationRequest().getDeliveryTime();
            if (isExpired(orderDeliveryTime)) {
                final Order expiredOrder = orderService.loadOrderEager(order.getId());

                //attach default expired bookingResponse
                for (BookingRequest bookingRequest : expiredOrder.getBookingRequests()) {
                    if (bookingRequest.getBookingResponse() == null) {
                        final BookingResponse expiredBookingResponse = new BookingResponse(bookingRequest,
                                BookingRequestEnum.Status.EXPIRED, TimeService.getCurrentTimestamp());
                        bookingRequest.applyBookingResponse(expiredBookingResponse);
                    }
                }

                expiredOrder.setOrderStatus(Order.OrderStatus.EXPIRED);
                orderService.updateOrder(expiredOrder);
                iterator.remove();
            }
        }
    }

    private boolean isExpired(DateTime orderDeliveryTime) {
        return TimeService.getCurrentTimestamp().isAfter(orderDeliveryTime);
    }

    private List<Order> fetchOrdersToProcess() {
        List<Order> newOrders = orderService.findAllByOrderStatus(Order.OrderStatus.NEW);
        newOrders.addAll(orderService.findAllByOrderStatus(Order.OrderStatus.DECLINED));
        return newOrders;
    }

    private BookingRequest createBookingRequest(final Order order, final TaxiDispatcher taxiDispatcher) {
        final Double payment = PriceService.calculateTaxiServicePayment(order.getReservationRequest());

        DateTime requestExpirationTime = TimeService.getCurrentTimestamp().plusMinutes(
                BOOKING_REQUEST_MINUTES_EXPIRATION);
        DateTime taxiDeliveryTime = order.getReservationRequest().getDeliveryTime();
        if (requestExpirationTime.isAfter(taxiDeliveryTime)) {
            requestExpirationTime = taxiDeliveryTime;
        }

        final DateTime currentTimestamp = TimeService.getCurrentTimestamp();
        return new BookingRequest(order, taxiDispatcher, payment, requestExpirationTime, currentTimestamp);
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    private void sendBookingRequest(final TaxiDispatcher taxiDispatcher, final BookingRequest persistedBookingRequest) {
        final String xmlMessage = serializeBookingRequest(persistedBookingRequest);
        final String destination = taxiDispatcher.getJmsQueue();

        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(final Session session) throws JMSException {
                return session.createTextMessage(xmlMessage);
            }
        });
    }

    private String serializeBookingRequest(final BookingRequest persistedBookingRequest) {
        final BookingRequestMessage bookingRequestMessage = new BookingRequestMessage(persistedBookingRequest);
        return xmlSerializer.serialize(bookingRequestMessage);
    }

}
