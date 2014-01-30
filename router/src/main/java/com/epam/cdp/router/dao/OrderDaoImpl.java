package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Geniy00
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public Order saveOrUpdate(Order order) {
        Order order1 =  em.merge(order);
        em.flush();
        return order1;
    }

    @Override
    public Order find(String id) {
        return em.find(Order.class, id);
    }

    @Override
    public void delete(Order order) {
        em.remove(em.merge(order));
    }
}
