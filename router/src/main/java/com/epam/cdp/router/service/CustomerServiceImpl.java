package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.router.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Override
    public Customer enrichCustomer(Customer customer) {
        return customerDao.saveOrUpdate(customer);
    }
}
