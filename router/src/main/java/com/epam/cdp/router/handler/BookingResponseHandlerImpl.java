package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.router.dao.OrderDao;
import com.epam.cdp.router.service.TimeService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class BookingResponseHandlerImpl implements BookingResponseHandler {

    private static final Logger LOG = Logger.getLogger(BookingResponseHandlerImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Override
    public BookingRequestEnum.Status handleAcceptCommand(final String orderId, final Long bookingRequestId)
            throws TsException {
        final BookingRequest bookingRequest = findBookingRequestByCriteria(bookingRequestId, orderId);

        if (!canExecuteAction(bookingRequest, BookingRequestEnum.Action.ACCEPT)) {
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, String.format(
                    "Can't change bookingRequest[bookingRequestId: %s, orderId: %s] status to %s", bookingRequestId,
                    orderId, BookingRequestEnum.Action.ACCEPT.name())
            );
        }

        final BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.ACCEPTED,
                TimeService.getCurrentTimestamp());
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(bookingRequest.getOrder());
        return BookingRequestEnum.Status.ACCEPTED;
    }

    @Override
    public BookingRequestEnum.Status handleRejectCommand(final String orderId, final Long bookingRequestId)
            throws TsException {
        BookingRequest bookingRequest = findBookingRequestByCriteria(bookingRequestId, orderId);

        if (!canExecuteAction(bookingRequest, BookingRequestEnum.Action.REJECT)) {
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, String.format(
                    "Can't change bookingRequest[bookingRequestId: %s, orderId: %s] status to %s", bookingRequestId,
                    orderId, BookingRequestEnum.Action.REJECT.name())
            );
        }

        final BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.REJECTED,
                TimeService.getCurrentTimestamp());
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(bookingRequest.getOrder());
        return BookingRequestEnum.Status.REJECTED;
    }

    @Override
    public BookingRequestEnum.Status handleRefuseCommand(final String orderId, final Long bookingRequestId,
            final String reason) throws TsException {
        BookingRequest bookingRequest = findBookingRequestByCriteria(bookingRequestId, orderId);

        if (!canExecuteAction(bookingRequest, BookingRequestEnum.Action.REFUSE)) {
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, String.format(
                    "Can't change bookingRequest[bookingRequestId: %s, orderId: %s] status to %s", bookingRequestId,
                    orderId, BookingRequestEnum.Action.REFUSE.name())
            );
        }

        final BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.REFUSED,
                reason, TimeService.getCurrentTimestamp());
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(bookingRequest.getOrder());
        return BookingRequestEnum.Status.REFUSED;
    }

    @Override
    public Customer getCustomerInfo(final String orderId, final Long bookingRequestId) throws TsException {
        BookingRequest bookingRequest = findBookingRequestByCriteria(bookingRequestId, orderId);

        final Order order = bookingRequest.getOrder();
        if (order.getOrderStatus() != Order.OrderStatus.PROCESSED) {
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL,
                    "Customer info could be received only after order is in PROCESSED status");
        }

        return order.getCustomer();
    }

    //TODO: can I extract this method?
    private boolean canExecuteAction(final BookingRequest bookingRequest, final BookingRequestEnum.Action action) {
        final Order.OrderStatus currentOrderStatus = bookingRequest.getOrder().getOrderStatus();

        switch (action) {
        case ACCEPT:
        case REJECT:
            final Boolean isNotExpired = isNotExpiredBookingRequest(bookingRequest);
            return currentOrderStatus == Order.OrderStatus.SENT && isNotExpired;

        case REFUSE:
            return currentOrderStatus == Order.OrderStatus.PROCESSED;

        case FAIL:
            return true;

        default:
            LOG.error(String.format("Unexpected action can't be executed on bookingRequest[%d]",
                    bookingRequest.getId()));
            return false;
        }
    }

    private BookingRequest findBookingRequestByCriteria(final Long bookingRequestId, final String orderId)
            throws TsException {
        final BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);
        if (bookingRequest == null) {
            final String message = String.format("BookingRequest with id:%d is null", bookingRequestId);
            LOG.warn(message);
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, message);
        }

        if (!bookingRequest.getOrder().getId().equals(orderId) || !bookingRequest.getId().equals(bookingRequestId)) {
            final String message = String.format("orderId:%s and bookingRequestId:%d are not linked", orderId,
                    bookingRequestId);
            LOG.warn(message);
            throw new TsException(TsException.Reason.PRE_CONDITION_CHECK_FAIL, message);
        }
        return bookingRequest;
    }

    private Boolean isNotExpiredBookingRequest(final BookingRequest bookingRequest) {
        final DateTime taxiDeliveryTime = bookingRequest.getOrder().getReservationRequest().getDeliveryTime();
        final DateTime currentTime = TimeService.getCurrentTimestamp();
        return bookingRequest.getExpiryTime().isAfter(currentTime) && taxiDeliveryTime.isAfter(currentTime);
    }
}
