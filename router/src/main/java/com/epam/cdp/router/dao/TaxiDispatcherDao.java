package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.TaxiDispatcher;

import java.util.List;

public interface TaxiDispatcherDao {

    List<TaxiDispatcher> findAllTaxiDispatchers();

    List<TaxiDispatcher> findActiveTaxiDispatchers();

}
