package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.router.dao.TaxiDispatcherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @author Geniy00
 */
@Service
public class TaxiDispatcherSelector {

    private final Random random = new Random();

    @Autowired
    TaxiDispatcherDao taxiDispatcherDao;

    public TaxiDispatcher selectTaxiDispatcher(final Order order) {
        final List<TaxiDispatcher> activeDispatchers = taxiDispatcherDao.findActiveTaxiDispatchers();
        final int dispatcherCount = activeDispatchers.size();

        if (dispatcherCount == 0) {
            return null;
        }

        final int index = random.nextInt(dispatcherCount);
        return activeDispatchers.get(index);
    }

}
