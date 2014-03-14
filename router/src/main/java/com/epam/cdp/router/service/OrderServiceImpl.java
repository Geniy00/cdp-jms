package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.*;
import com.epam.cdp.router.dao.CustomerDao;
import com.epam.cdp.router.dao.OrderDao;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Boolean isOrderActual(Long id) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(id);
        Boolean isNotExpired = new DateTime().isAfter(bookingRequest.getExpiryTime());

        return isNotExpired;
    }

    @Override
    public Customer acceptOrder(Long id) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(id);

        if (bookingRequest == null || !isOrderActual(id)) {
            LOG.warn("bookingRequest with id:" + id + " is null or it\'s expired");
            return null;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingResponse.BookingResponseStatus.ACCEPTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return order.getCustomer();
    }

    @Override
    public Boolean rejectOrder(Long id) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(id);

        if (bookingRequest == null || !isOrderActual(id)) {
            LOG.warn("bookingRequest with id:" + id + " is null or it\'s expired");
            return false;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingResponse.BookingResponseStatus.REJECTED);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return true;
    }

    @Override
    public Boolean refuseOrder(Long id, String reason) {
        BookingRequest bookingRequest = orderDao.findBookingRequest(id);

        if (bookingRequest == null || !isOrderActual(id)) {
            LOG.warn("bookingRequest with id:" + id + " is null or it\'s expired");
            return false;
        }

        Order order = bookingRequest.getOrder();
        BookingResponse bookingResponse = new BookingResponse(bookingRequest,
                BookingResponse.BookingResponseStatus.REFUSED, reason);
        bookingRequest.applyBookingResponse(bookingResponse);
        orderDao.saveOrUpdate(order);
        return true;
    }

    @Override
    public List<Order> findAllByOrderStatus(Order.OrderStatus status) {
        return orderDao.findAllByOrderStatus(status);
    }
}
