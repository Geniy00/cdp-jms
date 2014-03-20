package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.Order;
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

    private static final String SELECT_ALL_ORDERS = "SELECT ord FROM Order ord ORDER BY b.bookingRequest.deliveryTime desc";
    private static final String SELECT_ORDER_BY_STATUS = "SELECT ord FROM Order ord WHERE ord.status=:status ORDER BY b.bookingRequest.deliveryTime desc";

    @PersistenceContext
    EntityManager em;

    @Override
    public Order find(String id) {
        return em.find(Order.class, id);
    }

    @Override
    public List<Order> findAllOrders(int limit) {
        TypedQuery<Order> query = em.createQuery(SELECT_ALL_ORDERS, Order.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Order> findOrderByStatus(Order.OrderStatus status, int limit) {
        TypedQuery<Order> query = em.createQuery(SELECT_ORDER_BY_STATUS, Order.class);
        query.setParameter("status", status);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
