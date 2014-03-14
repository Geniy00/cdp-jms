package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.router.dao.TaxiDispatcherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class TaxiDispatcherSelector {

    @Autowired
    TaxiDispatcherDao taxiDispatcherDao;

    //TODO: this class is a stub. It has to be rewritten
    public TaxiDispatcher selectTaxiDispatcher(Order Order) {
        return taxiDispatcherDao.find(1L);
    }

}
