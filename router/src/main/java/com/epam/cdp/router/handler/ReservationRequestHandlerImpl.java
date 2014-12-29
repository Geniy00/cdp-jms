package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.router.dao.ReservationRequestDao;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.PriceService;
import com.epam.cdp.router.service.TimeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class ReservationRequestHandlerImpl implements ReservationRequestHandler {

    private static final Logger LOG = Logger.getLogger(ReservationRequestHandlerImpl.class);

    @Autowired
    private ReservationRequestDao requestDao;

    @Autowired
    private OrderService orderService;

    @Override
    public ReservationResponse handleIndicativeRequest(final ReservationRequest reservationRequest) {
        if (isValidIndicativeRequest(reservationRequest)) {
            final ReservationRequest pricedReservationRequest = priceReservationRequest(reservationRequest);
            requestDao.saveOrUpdate(pricedReservationRequest);
            return new ReservationResponse(pricedReservationRequest.getRequestId(),
                    pricedReservationRequest.getStatus(), pricedReservationRequest.getPrice());
        } else {
            final String reason = "ReservationRequest is invalid";
            return new ReservationResponse(reservationRequest.getRequestId(), true, reason);
        }
    }

    @Override
    public ReservationResponse handleOrderingRequest(final ReservationRequest reservationRequest) {
        if (isValidOrderingRequest(reservationRequest)) {
            final ReservationRequest persistedReservationRequest = requestDao.findByRequestId(
                    reservationRequest.getRequestId());
            final Order order = orderService.createAndSaveNewOrder(persistedReservationRequest);
            LOG.info(String.format("Order[id: %s] was created successfully", order.getId()));

            //TODO: run sending order to taxi service right here

            return new ReservationResponse(reservationRequest.getRequestId(), ReservationRequest.Status.RECEIVED,
                    reservationRequest.getPrice());
        } else {
            final String message = "Ordering reservation request didn't meet validation checking";
            LOG.warn(message);
            return new ReservationResponse(reservationRequest.getRequestId(), true, message);
        }
    }

    private ReservationRequest priceReservationRequest(final ReservationRequest reservationRequest) {
        final Double price = PriceService.calculateClientPrice(reservationRequest);
        final ReservationRequest pricedReservationRequest = new ReservationRequest(reservationRequest,
                TimeService.getCurrentTimestamp());
        pricedReservationRequest.setPrice(price);
        pricedReservationRequest.setStatus(ReservationRequest.Status.PRICED);
        return pricedReservationRequest;
    }

    private boolean isValidIndicativeRequest(final ReservationRequest reservationRequest) {
        return reservationRequest.getRequestId() != null
                && reservationRequest.getCustomerName().length() > 2
                && reservationRequest.getCustomerPhone().length() > 8
                && reservationRequest.getStartPosition() > 0
                && reservationRequest.getStartPosition() < 100
                && reservationRequest.getFinishPosition() > 0
                && reservationRequest.getFinishPosition() < 100
                && reservationRequest.getDeliveryTime().isAfter(TimeService.getCurrentTimestamp().plusMinutes(1))
                && reservationRequest.getPrice() == null
                && reservationRequest.isIndicative()
                && reservationRequest.getStatus() == ReservationRequest.Status.DRAFT;
    }

    private boolean isValidOrderingRequest(final ReservationRequest reservationRequest) {
        if (reservationRequest.getPrice() == null
                || reservationRequest.isIndicative()
                || reservationRequest.getStatus() != ReservationRequest.Status.PRICED) {
            return false;
        }
        final ReservationRequest persistedReservationRequest = requestDao.findByRequestId(
                reservationRequest.getRequestId());
        return persistedReservationRequest.equals(reservationRequest);
    }
}
