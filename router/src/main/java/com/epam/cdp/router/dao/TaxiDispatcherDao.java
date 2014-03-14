package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.TaxiDispatcher;

import java.util.List;

public interface TaxiDispatcherDao {

    TaxiDispatcher find(Long id);

    List<TaxiDispatcher> findAllTaxiDispatchers();

    List<TaxiDispatcher> findActiveTaxiDispatchers();

}
