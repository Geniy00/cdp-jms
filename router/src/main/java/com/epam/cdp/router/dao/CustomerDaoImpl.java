package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
public class CustomerDaoImpl implements CustomerDao {

    private static final String SELECT_CUSTOMER_BY_PHONE_NUMBER = "SELECT c FROM Customer c WHERE c.phone=:phone";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Customer saveOrUpdate(Customer customer) {

        if (customer.getId() != null) {
            if (find(customer.getId()) != null) {
                return em.merge(customer);
            }
        }

        final Customer originalCustomer = findCustomerByPhoneNumber(customer.getPhone());
        if (originalCustomer != null) {
            customer.setId(originalCustomer.getId());
            return em.merge(customer);
        }

        return em.merge(customer);
    }

    @Override
    public Customer find(Long id) {
        //TODO: remove fake customer
        if (id == -1) {
            return new Customer("80639512345", "TestUser");
        }
        return em.find(Customer.class, id);
    }

    @Override
    public Customer findCustomerByPhoneNumber(String phoneNumber) {
        final TypedQuery<Customer> query = em.createQuery(SELECT_CUSTOMER_BY_PHONE_NUMBER, Customer.class);
        query.setParameter("phone", phoneNumber);
        final List<Customer> results = query.getResultList();
        if (results.size() == 0) {
            return null;
        }
        if (results.size() > 1) {
            throw new NonUniqueResultException();
        }
        return results.get(0);
    }

    @Override
    public void delete(Customer customer) {
        em.remove(em.merge(customer));
    }
}
