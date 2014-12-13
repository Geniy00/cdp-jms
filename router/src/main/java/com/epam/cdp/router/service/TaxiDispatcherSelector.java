package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.router.dao.TaxiDispatcherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Geniy00
 */
@Service
public class TaxiDispatcherSelector {

    @Autowired
    TaxiDispatcherDao taxiDispatcherDao;

    public TaxiDispatcher selectTaxiDispatcher(Order Order) {
        List<TaxiDispatcher> activeDispatchers = taxiDispatcherDao.findActiveTaxiDispatchers();
        int activeCount = activeDispatchers.size();

        if (activeCount == 0) {
            return null;
        }

        int index = (int) (Math.random() * activeCount);
        return activeDispatchers.get(index);
    }

}
