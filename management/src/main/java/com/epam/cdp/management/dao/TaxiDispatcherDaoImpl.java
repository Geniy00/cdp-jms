package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.TaxiDispatcher;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
public class TaxiDispatcherDaoImpl implements TaxiDispatcherDao {

    private static final String SELECT_ALL_TAXI_DISPATCHER = "SELECT td FROM TaxiDispatcher td";

    @PersistenceContext
    EntityManager em;

    @Override
    public TaxiDispatcher saveOrUpdate(final TaxiDispatcher taxiDispatcher) {
        return em.merge(taxiDispatcher);
    }

    @Override
    public void delete(final TaxiDispatcher taxiDispatcher) {
        em.remove(em.merge(taxiDispatcher));
    }

    @Override
    public TaxiDispatcher find(final Long id) {
        return em.find(TaxiDispatcher.class, id);
    }

    @Override
    public List<TaxiDispatcher> findAll() {
        final TypedQuery<TaxiDispatcher> query = em.createQuery(SELECT_ALL_TAXI_DISPATCHER, TaxiDispatcher.class);
        return query.getResultList();
    }
}
