package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.router.dao.CustomerDao;
import com.epam.cdp.router.dao.OrderDao;
import com.epam.cdp.router.dao.ReservationRequestDao;
import com.epam.cdp.router.gateway.ReservationRequestGateway;
import com.epam.cdp.router.handler.BookingRequestHandler;
import com.epam.cdp.router.handler.BookingResponseHandler;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);
    public static final int PRICED_REQUEST_IS_EXPIRED_IN_MINS = 5;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ReservationRequestDao reservationRequestDao;

    @Autowired
    private BookingRequestHandler bookingRequestHandler;

    @Autowired
    private BookingResponseHandler bookingResponseHandler;

    @Autowired
    private ReservationRequestGateway reservationRequestGateway;

    @Override
    public Order createAndSaveNewOrder(final Long requestId) {
        final ReservationRequest reservationRequest = reservationRequestDao.findByRequestId(requestId);
        final Customer customer = getPersistedCustomer(reservationRequest);
        final String id = UUID.randomUUID().toString();
        reservationRequest.setStatus(ReservationRequest.Status.RECEIVED);
        final Order order = new Order(id, customer, reservationRequest);
        return orderDao.saveOrUpdate(order);
    }

    @Override
    public Order update(final Order order) {
        return orderDao.saveOrUpdate(order);
    }

    @Override
    public ReservationRequest update(final ReservationRequest reservationRequest) {
        return reservationRequestDao.saveOrUpdate(reservationRequest);
    }

    @Override
    public void delete(final Order order) {
        orderDao.delete(order);
    }

    @Override
    public Order find(final String id) {
        return orderDao.find(id);
    }

    @Override
    public List<Order> findAllByOrderStatus(final Order.OrderStatus status) {
        return orderDao.findAllByOrderStatus(status);
    }

    @Override
    public void terminateExpiredOrders() {
        final List<Order> expiredOrders = orderDao.findExpiredOrders();
        for (final Order expiredOrder : expiredOrders) {
            expiredOrder.setOrderStatus(Order.OrderStatus.EXPIRED);
            orderDao.saveOrUpdate(expiredOrder);
            LOG.info(String.format("Order[id:%s] is expired", expiredOrder.getId()));
        }
    }

    @Override
    public void terminateExpiredBookingRequests() {
        final List<BookingRequest> expiredBookingRequests = orderDao.findExpiredBookingRequests();
        for (final BookingRequest expiredBookingRequest : expiredBookingRequests) {
            final BookingResponse bookingResponse = new BookingResponse(expiredBookingRequest,
                    BookingRequest.Status.EXPIRED, "automatically expired", TimeService.getCurrentTimestamp());
            expiredBookingRequest.applyBookingResponse(bookingResponse);

            expiredBookingRequest.getOrder().setOrderStatus(Order.OrderStatus.DECLINED);

            orderDao.updateBookingRequest(expiredBookingRequest);
            LOG.info(String.format("BookingRequest[id:%d] is expired", expiredBookingRequest.getId()));
        }
    }

    @Override
    public void terminateExpiredPricedRequests() {
        final DateTime expiredBefore = TimeService.getCurrentTimestamp().minusMinutes(
                PRICED_REQUEST_IS_EXPIRED_IN_MINS);
        final List<ReservationRequest> expiredPricedRequests = reservationRequestDao.findExpiredPricedRequests(
                expiredBefore);
        for (ReservationRequest pricedRequest : expiredPricedRequests) {
            reservationRequestDao.delete(pricedRequest.getId());
        }
        if (expiredPricedRequests.size() > 0) {
            LOG.info(String.format("Removed %s expired priced requests", expiredPricedRequests.size()));
        }
    }

    @Override
    public BookingRequest updateBookingRequest(final BookingRequest bookingRequest) {
        return orderDao.updateBookingRequest(bookingRequest);
    }

    @Override
    public Customer getCustomerInfo(final String orderId, final Long bookingRequestId) throws TsException {
        return bookingResponseHandler.getCustomerInfo(orderId, bookingRequestId);
    }

    @Override
    public BookingRequest.Status acceptOrder(final String orderId, final Long bookingRequestId) throws TsException {
        final BookingRequest.Status newStatus = bookingResponseHandler.handleAcceptCommand(orderId,
                bookingRequestId);
        if (newStatus == BookingRequest.Status.ACCEPTED) {
            sendReservationResponseOnOrderUpdate(orderId, ReservationRequest.Status.COMPLETED, "");
        }
        return newStatus;
    }

    @Override
    public BookingRequest.Status rejectOrder(String orderId, Long bookingRequestId) throws TsException {
        final BookingRequest.Status newStatus = bookingResponseHandler.handleRejectCommand(orderId,
                bookingRequestId);
        if (newStatus == BookingRequest.Status.REJECTED) {
            sendReservationResponseOnOrderUpdate(orderId, ReservationRequest.Status.ASSIGNING, "");
            final Order order = orderDao.find(orderId);
            bookingRequestHandler.sendBookingRequest(order);
        }
        return newStatus;
    }

    @Override
    public BookingRequest.Status refuseOrder(final String orderId, final Long bookingRequestId,
            final String reason) throws TsException {
        final BookingRequest.Status newStatus = bookingResponseHandler.handleRefuseCommand(orderId,
                bookingRequestId, reason);
        if (newStatus == BookingRequest.Status.REFUSED) {
            sendReservationResponseOnOrderUpdate(orderId, ReservationRequest.Status.ASSIGNING, "");
        }
        return newStatus;
    }

    @Override
    public Order loadOrderEager(final String id) {
        return orderDao.loadOrderEager(id);
    }

    @Override
    public void persistFailQueueMessage(final FailQueueMessage failQueueMessage) {
        //TODO: move this method to right place
        orderDao.persistFailQueueMessage(failQueueMessage);
    }

    private void sendReservationResponseOnOrderUpdate(final String orderId, final ReservationRequest.Status newStatus,
            final String failureReason) {
        final Order order = orderDao.find(orderId);
        final ReservationRequest reservationRequest = order.getReservationRequest();
        final ReservationResponse reservationResponse;
        final Long requestId = reservationRequest.getRequestId();
        if (newStatus != ReservationRequest.Status.FAILURE) {
            reservationResponse = new ReservationResponse(requestId, newStatus, reservationRequest.getPrice());
        } else {
            reservationResponse = new ReservationResponse(requestId, true, failureReason);
        }
        reservationRequestGateway.send(reservationRequest.getSourceSystem().getJmsResponseQueue(), reservationResponse);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Customer getPersistedCustomer(final ReservationRequest reservationRequest) {
        final Customer persistedCustomer = customerDao.findCustomerByPhoneNumber(reservationRequest.getCustomerPhone());
        if (persistedCustomer == null) {
            final Customer customer = new Customer(reservationRequest.getCustomerPhone(),
                    reservationRequest.getCustomerName());
            return customerDao.saveOrUpdate(customer);
        } else {
            return persistedCustomer;
        }
    }


}
