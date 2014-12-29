package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.TaxiDispatcher;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TaxiDispatcherDaoImpl implements TaxiDispatcherDao {

    private static final String SELECT_ALL_TAXI_DISPATCHERS = "SELECT td FROM TaxiDispatcher td";
    private static final String SELECT_ACTIVE_TAXI_DISPATCHERS =
            "SELECT td FROM TaxiDispatcher td WHERE td.disabled=false";

    @PersistenceContext
    private EntityManager em;

    @Override
    public TaxiDispatcher find(Long id) {
        return em.find(TaxiDispatcher.class, id);
    }

    @Override
    public List<TaxiDispatcher> findAllTaxiDispatchers() {
        final TypedQuery<TaxiDispatcher> query = em.createQuery(SELECT_ALL_TAXI_DISPATCHERS, TaxiDispatcher.class);
        return query.getResultList();
    }

    @Override
    public List<TaxiDispatcher> findActiveTaxiDispatchers() {
        final TypedQuery<TaxiDispatcher> query = em.createQuery(SELECT_ACTIVE_TAXI_DISPATCHERS, TaxiDispatcher.class);
        return query.getResultList();
    }
}
