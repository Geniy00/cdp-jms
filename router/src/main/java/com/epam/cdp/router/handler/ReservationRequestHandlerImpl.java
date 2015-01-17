package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.router.dao.ReservationRequestDao;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.PriceService;
import com.epam.cdp.router.service.TimeService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Geniy00
 */
@Service
public class ReservationRequestHandlerImpl implements ReservationRequestHandler {

    private static final Logger LOG = Logger.getLogger(ReservationRequestHandlerImpl.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Autowired
    private ReservationRequestDao requestDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookingRequestHandler bookingRequestHandler;

    @Transactional
    @Override
    public ReservationResponse handleIndicativeRequest(final ReservationRequest reservationRequest) {
        try {
            validateIndicativeRequest(reservationRequest);

            final ReservationRequest pricedReservationRequest = priceReservationRequest(reservationRequest);
            orderService.update(pricedReservationRequest);
            return new ReservationResponse(pricedReservationRequest.getRequestId(),
                    pricedReservationRequest.getStatus(), pricedReservationRequest.getPrice());
        } catch (final TsException ex) {
            LOG.warn(ex);
            return new ReservationResponse(reservationRequest.getRequestId(), true, ex.getMessage());
        }
    }

    @Transactional
    @Override
    public ReservationResponse handleOrderingRequest(final ReservationRequest reservationRequest) {
        try {
            validateOrderingRequest(reservationRequest);


            final Order order = orderService.createAndSaveNewOrder(reservationRequest.getRequestId());
            LOG.info(String.format("Order %s on requestId %s was created successfully", order.getId(),
                    order.getReservationRequest().getRequestId()));

            asyncSendBookingRequest(order);

            return new ReservationResponse(reservationRequest.getRequestId(), ReservationRequest.Status.RECEIVED,
                    reservationRequest.getPrice());
        } catch (final TsException ex) {
            LOG.warn(ex);
            return new ReservationResponse(reservationRequest.getRequestId(), true, ex.getMessage());
        }
    }

    private void asyncSendBookingRequest(final Order order) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                bookingRequestHandler.sendBookingRequest(order);
            }
        });
    }

    private ReservationRequest priceReservationRequest(final ReservationRequest reservationRequest) {
        final Double price = PriceService.calculateClientPrice(reservationRequest);
        final ReservationRequest pricedReservationRequest = new ReservationRequest(reservationRequest,
                TimeService.getCurrentTimestamp());
        pricedReservationRequest.setPrice(price);
        pricedReservationRequest.setStatus(ReservationRequest.Status.PRICED);
        return pricedReservationRequest;
    }

    private void validateIndicativeRequest(final ReservationRequest reservationRequest) throws TsException {
        final Long requestId = reservationRequest.getRequestId();
        final String customerName = reservationRequest.getCustomerName();
        final String customerPhone = reservationRequest.getCustomerPhone();
        final Integer startPosition = reservationRequest.getStartPosition();
        final Integer finishPosition = reservationRequest.getFinishPosition();
        final DateTime deliveryTime = reservationRequest.getDeliveryTime();
        final Double price = reservationRequest.getPrice();
        final ReservationRequest.Status status = reservationRequest.getStatus();

        if (requestId == null) {
            throwValidationException("requestId mustn't be null", "null");
        } else if (customerName.length() < 2) {
            throwValidationException("customerName must be at least 2 symbols", customerName);
        } else if (customerPhone.length() < 8) {
            throwValidationException("customerPhone must be at least 8 numbers", customerPhone);
        } else if (!between(startPosition, 0, 100)) {
            throwValidationException("Start position must be between 0 and 100", startPosition.toString());
        } else if (!between(finishPosition, 0, 100)) {
            throwValidationException("Finish position must be between 0 and 100", finishPosition.toString());
        } else if (deliveryTime.isBefore(TimeService.getCurrentTimestamp().plusMinutes(1))) {
            throwValidationException("Delivery time must be at least in a minute", deliveryTime.toString());
        } else if (price != null) {
            throwValidationException("Price must be null for indicative", price.toString());
        } else if (!reservationRequest.isIndicative()) {
            throwValidationException("Request must be indicative", "not indicative");
        } else if (status != ReservationRequest.Status.DRAFT) {
            throwValidationException("Request status must be DRAFT", status.name());
        }
    }

    private static boolean between(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return value > min && value < max;
    }

    private void validateOrderingRequest(final ReservationRequest reservationRequest) throws TsException {
        final Long requestId = reservationRequest.getRequestId();
        final Double price = reservationRequest.getPrice();
        final ReservationRequest.Status status = reservationRequest.getStatus();

        if (requestId == null) {
            throwValidationException("requestId mustn't be null", "null");
        } else if (price == null) {
            throwValidationException("Price mustn't be null", "null");
        } else if (reservationRequest.isIndicative()) {
            throwValidationException("Request mustn't be indicative", "indicative");
        } else if (status != ReservationRequest.Status.PRICED) {
            throwValidationException("Status must be PRICED", status.name());
        }

        final ReservationRequest persistedReservationRequest = requestDao.findByRequestId(requestId);
        if (!persistedReservationRequest.equals(reservationRequest)) {
            throw new TsException(TsException.Reason.VALIDATION_FAILURE, String.format(
                    "Persisted reservation request isn't the same as received. Persisted[%s], received[%s]",
                    persistedReservationRequest, reservationRequest));
        }
    }

    private void throwValidationException(final String reason, final String actualValue) throws TsException {
        final String ending = String.format(", but it's [%s]", actualValue);
        throw new TsException(TsException.Reason.VALIDATION_FAILURE, reason + ending);
    }
}
