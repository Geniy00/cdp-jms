package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.TaxiDispatcher;

import java.util.List;

/**
 * @author Geniy00
 */
public interface TaxiDispatcherService {

    TaxiDispatcher update(TaxiDispatcher taxiDispatcher);

    void delete(Long id);

    TaxiDispatcher find(Long id);

    List<TaxiDispatcher> findAll();
}
