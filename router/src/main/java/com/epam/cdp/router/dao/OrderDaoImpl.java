package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.router.service.TimeService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.epam.cdp.core.entity.Order.OrderStatus.*;

/**
 * @author Geniy00
 */
@Transactional(propagation = Propagation.MANDATORY)
@Repository
public class OrderDaoImpl implements OrderDao {

    private static final String SELECT_ORDER_BY_STATUS = "SELECT ord FROM Order ord WHERE ord.orderStatus=:status";

    private static final String SELECT_EXPIRED_BOOKING_REQUEST =
            "SELECT br FROM BookingRequest br WHERE br.expiryTime < :currentTime AND br.bookingResponse = null";

    private static final String SELECT_EXPIRED_ORDER = String.format("SELECT ord FROM Order ord " +
            "WHERE ord.reservationRequest.deliveryTime < :dateTime AND " +
            "(ord.orderStatus = '%s' OR ord.orderStatus = '%s' OR ord.orderStatus = '%s')",
            NEW.name(), SENT.name(), DECLINED.name());

    @PersistenceContext
    private EntityManager em;

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
        query.setParameter("dateTime", TimeService.getCurrentTimestamp());
        return query.getResultList();
    }

    @Override
    public List<BookingRequest> findExpiredBookingRequests() {
        TypedQuery<BookingRequest> query = em.createQuery(SELECT_EXPIRED_BOOKING_REQUEST, BookingRequest.class);
        query.setParameter("currentTime", TimeService.getCurrentTimestamp());
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
