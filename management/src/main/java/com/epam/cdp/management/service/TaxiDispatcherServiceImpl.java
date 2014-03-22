package com.epam.cdp.management.service;

import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.management.dao.TaxiDispatcherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Geniy00
 */
@Service
@Transactional
public class TaxiDispatcherServiceImpl implements TaxiDispatcherService {

    @Autowired
    TaxiDispatcherDao taxiDispatcherDao;

    @Override
    public TaxiDispatcher update(TaxiDispatcher taxiDispatcher) {
        return taxiDispatcherDao.saveOrUpdate(taxiDispatcher);
    }

    @Override
    public void delete(Long id) {
        TaxiDispatcher taxiDispatcher = taxiDispatcherDao.find(id);
        taxiDispatcherDao.delete(taxiDispatcher);
    }

    @Override
    public TaxiDispatcher find(Long id) {
        return taxiDispatcherDao.find(id);
    }

    @Override
    public List<TaxiDispatcher> findAll() {
        return taxiDispatcherDao.findAll();
    }
}
