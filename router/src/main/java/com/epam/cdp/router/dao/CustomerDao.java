package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.Customer;

/**
 * @author Geniy00
 */
public interface CustomerDao {

    Customer saveOrUpdate(Customer customer);

    Customer find(Long id);

    Customer findCustomerByPhoneNumber(String phoneNumber);

    void delete(Customer customer);

}
