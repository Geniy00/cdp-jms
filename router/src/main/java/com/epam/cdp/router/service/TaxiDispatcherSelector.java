package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.TaxiDispatcher;
import org.springframework.stereotype.Service;

/**
 * @author Geniy00
 */
@Service
public class TaxiDispatcherSelector {

    //TODO: this class is a stub. It has to be rewritten
    public TaxiDispatcher selectTaxiDispatcher(Order Order) {
        TaxiDispatcher taxiDispatcher = new TaxiDispatcher();
        taxiDispatcher.setId(1L);
        taxiDispatcher.setName("Aviz taxi");
        taxiDispatcher.setJmsQueue("aviz.input.queue");
        taxiDispatcher.setJmsQueueCapacity(5);
        taxiDispatcher.setEmail("aviz@gmail.com");
        taxiDispatcher.setDisabled(false);
        return taxiDispatcher;
    }

}
