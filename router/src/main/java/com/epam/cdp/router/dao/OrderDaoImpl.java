package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Order;
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

    private static final String SELECT_ORDER_BY_STATUS = "SELECT ord FROM Order ord where ord.orderStatus=:status";

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
    public void delete(Order order) {
        em.remove(em.merge(order));
    }

    @Override
    public List<Order> findAllByOrderStatus(Order.OrderStatus status) {
        TypedQuery<Order> query =
                em.createQuery(SELECT_ORDER_BY_STATUS, Order.class);
        query.setParameter("status", status);
        List<Order> result = query.getResultList();
        for (Order order : result) {
            Hibernate.initialize(order.getBookingRequests());
        }
        return result;
    }

}
