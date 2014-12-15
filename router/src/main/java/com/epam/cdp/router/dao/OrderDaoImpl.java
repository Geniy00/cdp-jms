package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.router.service.TimeService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    private static final String SELECT_ORDER_BY_STATUS = "SELECT ord FROM Order ord WHERE ord.orderStatus=:status";

    private static final String SELECT_EXPIRED_BOOKING_REQUEST =
            "SELECT br FROM BookingRequest br " + "WHERE br.expiryTime < :currentTime AND br.bookingResponse = null";

    //TODO: rewrite SQL query without NEW, SENT, DECLINED strings. Use Enum Name
    private static final String SELECT_EXPIRED_ORDER = "SELECT ord FROM Order ord" +
            "WHERE ord.reservationRequest.deliveryTime < :dateTime AND " +
            "(ord.orderStatus = 'NEW' OR ord.orderStatus = 'SENT' OR ord.orderStatus = 'DECLINED')";

    @PersistenceContext
    EntityManager em;

    @Override
    public Order saveOrUpdate(Order order) {
        return em.merge(order);
    }

    @Override
    public Order find(String id) {
        return em.find(Order.class, id);
    }

    @Override
    public BookingRequest updateBookingRequest(BookingRequest bookingRequest) {
        return em.merge(bookingRequest);
    }

    @Override
    public BookingRequest findBookingRequest(Long id) {
        return em.find(BookingRequest.class, id);
    }

    @Override
    public List<Order> findExpiredOrders() {
        final TypedQuery<Order> query = em.createQuery(SELECT_EXPIRED_ORDER, Order.class);
        query.setParameter("dateTime", TimeService.getCurrentDateTime());
        return query.getResultList();
    }

    @Override
    public List<BookingRequest> findExpiredBookingRequests() {
        TypedQuery<BookingRequest> query = em.createQuery(SELECT_EXPIRED_BOOKING_REQUEST, BookingRequest.class);
        query.setParameter("currentTime", TimeService.getCurrentDateTime());
        return query.getResultList();
    }

    @Override
    public void delete(Order order) {
        em.remove(em.merge(order));
    }

    @Override
    public List<Order> findAllByOrderStatus(Order.OrderStatus status) {
        final TypedQuery<Order> query = em.createQuery(SELECT_ORDER_BY_STATUS, Order.class);
        query.setParameter("status", status);
        final List<Order> result = query.getResultList();
        for (final Order order : result) {
            Hibernate.initialize(order.getBookingRequests());
        }
        return result;
    }

    @Override
    public Order loadOrderEager(String id) {
        final Order order = em.find(Order.class, id);
        Hibernate.initialize(order.getBookingRequests());
        return order;
    }

    //TODO: move this method to right class
    @Override
    public void persistFailQueueMessage(FailQueueMessage failQueueMessage) {
        em.persist(failQueueMessage);
    }

}
