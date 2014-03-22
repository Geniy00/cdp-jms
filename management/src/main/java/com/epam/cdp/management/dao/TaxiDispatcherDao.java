package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.TaxiDispatcher;

import java.util.List;

/**
 * @author Geniy00
 */
public interface TaxiDispatcherDao {

    TaxiDispatcher saveOrUpdate(TaxiDispatcher taxiDispatcher);

    void delete(TaxiDispatcher taxiDispatcher);

    TaxiDispatcher find(Long id);

    List<TaxiDispatcher> findAll();
}
