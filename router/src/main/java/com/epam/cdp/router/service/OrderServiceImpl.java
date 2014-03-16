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
    public Order createAndSaveNewOrder(ReservationRequest reservationRequest) {
        Customer customer = new Customer(reservationRequest.getCustomerPhone(), reservationRequest.getCustomerName());
        customer = customerDao.saveOrUpdate(customer);

        String id = UUID.randomUUID().toString();
        Order order = new Order(id, customer, reservationRequest);
        return orderDao.saveOrUpdate(order);
    }

    @Override
    public Order find(String id) {
        return orderDao.find(id);
    }

    @Override
    public void updateOrder(Order order) {
        orderDao.saveOrUpdate(order);
    }

    @Override
    public BookingRequest updateBookingRequest(BookingRequest bookingRequest) {
        return orderDao.updateBookingRequest(bookingRequest);
    }

    @Override
    public Boolean isOrderActual(String orderId, Long bookingRequestId) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (bookingRequest == null) {
            LOG.warn("bookingRequest with id:" + bookingRequestId + " is null");
            return false;
        }

        if (!bookingRequest.getOrder().getId().equals(orderId)
                || !bookingRequest.getId().equals(bookingRequestId)) {
            LOG.warn("orderId:" + orderId + " and bookingRequestId:" + bookingRequestId + " are not linked");
            return false;
        }

        if (new DateTime().isAfter(bookingRequest.getExpiryTime())) {
            LOG.warn("bookingRequestId:" + bookingRequestId + " is expired");
            return false;
        }

        return true;
    }

    @Override
    public Customer acceptOrder(String orderId, Long bookingRequestId) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if(!isOrderActual(orderId, bookingRequestId)) {
            return null;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingRequestEnum.Status.ACCEPTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return order.getCustomer();
    }

    @Override
    public BookingRequestEnum.Status rejectOrder(String orderId, Long bookingRequestId) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (!isOrderActual(orderId, bookingRequestId)) {
            return BookingRequestEnum.Status.EXPIRED;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingRequestEnum.Status.REJECTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return BookingRequestEnum.Status.REJECTED;
    }

    @Override
    public BookingRequestEnum.Status refuseOrder(String orderId, Long bookingRequestId, String reason) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(bookingRequestId);

        if (!isOrderActual(orderId, bookingRequestId)) {
            return BookingRequestEnum.Status.EXPIRED;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingRequestEnum.Status.REFUSED, reason);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return BookingRequestEnum.Status.REFUSED;
    }

    @Override
    public List<Order> findAllByOrderStatus(Order.OrderStatus status) {
        return orderDao.findAllByOrderStatus(status);
    }
}
