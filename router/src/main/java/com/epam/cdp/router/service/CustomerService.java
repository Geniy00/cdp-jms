package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Customer;

/**
 * @author Geniy00
 */
public interface CustomerService {

    /**
     * Enrich customer from external storage (DB)
     *
     * @param customer customer with partially filled fields
     * @return enriched customer from external storage
     */
    Customer enrichCustomer(Customer customer);
}
