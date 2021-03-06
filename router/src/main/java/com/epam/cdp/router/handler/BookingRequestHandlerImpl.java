package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.router.gateway.BookingRequestSender;
import com.epam.cdp.router.gateway.ReservationRequestGateway;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.PriceService;
import com.epam.cdp.router.service.TaxiDispatcherSelector;
import com.epam.cdp.router.service.TimeService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class BookingRequestHandlerImpl implements BookingRequestHandler {

    private static final Logger LOG = Logger.getLogger(BookingRequestHandlerImpl.class);

    @Value("${booking.request.expiration}")
    private int BOOKING_REQUEST_MINUTES_EXPIRATION;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaxiDispatcherSelector taxiDispatcherSelector;

    @Autowired
    private BookingRequestSender requestSender;

    @Autowired
    private ReservationRequestGateway reservationRequestGateway;

    @Override
    public void sendBookingRequest(final Order order) {
        final TaxiDispatcher taxiDispatcher = taxiDispatcherSelector.selectTaxiDispatcher(order);
        if (taxiDispatcher == null) {
            LOG.warn("Can't find any active taxi dispatcher. BookingRequest sending was postponed");
            return;
        }

        //Create xml message from BookingRequest attribute of Order
        final BookingRequest bookingRequest = createBookingRequest(order, taxiDispatcher);
        final BookingRequest persistedBookingRequest = orderService.updateBookingRequest(bookingRequest);

        requestSender.send(taxiDispatcher, persistedBookingRequest);

        order.applyBookingRequest(persistedBookingRequest);
        final Order persistedOrder = orderService.update(order);

        sendReservationResponseOnOrderUpdate(persistedOrder);
    }

    private void sendReservationResponseOnOrderUpdate(final Order persistedOrder) {
        final ReservationRequest reservationRequest = persistedOrder.getReservationRequest();
        final String destination = reservationRequest.getSourceSystem().getJmsResponseQueue();
        final ReservationResponse reservationResponse = new ReservationResponse(reservationRequest.getRequestId(),
                ReservationRequest.Status.ASSIGNED, reservationRequest.getPrice());
        reservationRequestGateway.send(destination, reservationResponse);
    }

    private BookingRequest createBookingRequest(final Order order, final TaxiDispatcher taxiDispatcher) {
        final Double payment = PriceService.calculateTaxiServicePayment(order.getReservationRequest());

        DateTime requestExpirationTime = TimeService.getCurrentTimestamp().plusMinutes(
                BOOKING_REQUEST_MINUTES_EXPIRATION);
        final DateTime taxiDeliveryTime = order.getReservationRequest().getDeliveryTime();
        if (requestExpirationTime.isAfter(taxiDeliveryTime)) {
            requestExpirationTime = taxiDeliveryTime;
        }

        final DateTime currentTimestamp = TimeService.getCurrentTimestamp();
        return new BookingRequest(order, taxiDispatcher, payment, requestExpirationTime, currentTimestamp);
    }
}
