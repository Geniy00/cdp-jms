package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.router.dao.CustomerDao;
import com.epam.cdp.router.dao.OrderDao;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    public static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    CustomerDao customerDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public Order createAndSaveNewOrder(final ReservationRequest reservationRequest) {
        final Customer customer = getPersistedCustomer(reservationRequest);
        final String id = UUID.randomUUID().toString();
        final Order order = new Order(id, customer, reservationRequest);
        return orderDao.saveOrUpdate(order);
    }

    @Override
    public Order update(final Order order) {
        return orderDao.saveOrUpdate(order);
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
            LOG.info("Order[id:" + expiredOrder.getId() + "] is expired");
        }
    }

    @Override
    public void terminateExpiredBookingRequests() {
        final List<BookingRequest> expiredBookingRequests = orderDao.findExpiredBookingRequests();
        for (final BookingRequest expiredBookingRequest : expiredBookingRequests) {
            final BookingResponse bookingResponse = new BookingResponse(expiredBookingRequest,
                    BookingRequestEnum.Status.EXPIRED, "automatically expired");
            expiredBookingRequest.applyBookingResponse(bookingResponse);

            expiredBookingRequest.getOrder().setOrderStatus(Order.OrderStatus.DECLINED);

            orderDao.updateBookingRequest(expiredBookingRequest);
            LOG.info("BookingRequest[id:" + expiredBookingRequest.getId() + "] is expired");
        }
    }

    @Override
    public void updateOrder(final Order order) {
        orderDao.saveOrUpdate(order);
    }

    @Override
    public BookingRequest updateBookingRequest(final BookingRequest bookingRequest) {
        return orderDao.updateBookingRequest(bookingRequest);
    }

    @Override
    public Customer acceptOrder(final String orderId, final Long bookingRequestId) {
        final BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (!isOrderActual(orderId, bookingRequestId) || !canExecuteAction(bookingRequest,
                BookingRequestEnum.Action.ACCEPT)) {
            return null;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.ACCEPTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return order.getCustomer();
    }

    @Override
    public BookingRequestEnum.Status rejectOrder(String orderId, Long bookingRequestId) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (!isOrderActual(orderId, bookingRequestId) || !canExecuteAction(bookingRequest,
                BookingRequestEnum.Action.REJECT)) {
            return BookingRequestEnum.Status.EXPIRED;
        }

        final Order order = bookingRequest.getOrder();
        final BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.REJECTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return BookingRequestEnum.Status.REJECTED;
    }

    @Override
    public BookingRequestEnum.Status refuseOrder(final String orderId, final Long bookingRequestId,
            final String reason) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (!isOrderActual(orderId, bookingRequestId) || !canExecuteAction(bookingRequest,
                BookingRequestEnum.Action.REFUSE)) {
            //TODO: It shouldn't return EXPIRED!
            return BookingRequestEnum.Status.EXPIRED;
        }

        final Order order = bookingRequest.getOrder();
        final BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.REFUSED,
                reason);
        bookingRequest.applyBookingResponse(bookingResponse);
        order.setOrderStatus(Order.OrderStatus.CANCELED);
        orderDao.saveOrUpdate(order);
        return BookingRequestEnum.Status.REFUSED;
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

    private Boolean isOrderActual(final String orderId, final Long bookingRequestId) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (bookingRequest == null) {
            LOG.warn("bookingRequest with id:" + bookingRequestId + " is null");
            return false;
        }

        if (!bookingRequest.getOrder().getId().equals(orderId) || !bookingRequest.getId().equals(bookingRequestId)) {
            LOG.warn("orderId:" + orderId + " and bookingRequestId:" + bookingRequestId + " are not linked");
            return false;
        }

        return true;
    }

    //TODO: can I extract this method?
    private Boolean canExecuteAction(final BookingRequest bookingRequest, final BookingRequestEnum.Action action) {
        Order.OrderStatus orderStatus = bookingRequest.getOrder().getOrderStatus();

        switch (action) {
        case ACCEPT:
        case REJECT:
            final Boolean isNotExpired = isNotExpiredBookingRequest(bookingRequest);
            return orderStatus == Order.OrderStatus.SENT && isNotExpired;

        case REFUSE:
            return orderStatus == Order.OrderStatus.PROCESSED;

        case FAIL:
            return true;

        default:
            LOG.error("Unexpected action can't be executed on bookingRequest[" + bookingRequest.getId() + "]");
            return false;
        }
    }

    private Boolean isNotExpiredBookingRequest(final BookingRequest bookingRequest) {
        final DateTime taxiDeliveryTime = bookingRequest.getOrder().getReservationRequest().getDeliveryTime();
        final DateTime currentTime = TimeService.getCurrentDateTime();
        return bookingRequest.getExpiryTime().isAfter(currentTime) && taxiDeliveryTime.isAfter(currentTime);
    }
}
