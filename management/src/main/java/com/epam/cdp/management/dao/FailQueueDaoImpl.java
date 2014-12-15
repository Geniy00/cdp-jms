package com.epam.cdp.management.dao;

import com.epam.cdp.core.entity.FailQueueMessage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
public class FailQueueDaoImpl implements FailQueueDao {

    private static final String SELECT_ALL_FAIL_QUEUE = "SELECT fqm FROM FailQueueMessage fqm";

    @PersistenceContext
    EntityManager em;

    @Override
    public FailQueueMessage saveOrUpdate(final FailQueueMessage failQueueMessage) {
        return em.merge(failQueueMessage);
    }

    @Override
    public void delete(final FailQueueMessage failQueueMessage) {
        em.remove(em.merge(failQueueMessage));
    }

    @Override
    public FailQueueMessage find(final Long id) {
        return em.find(FailQueueMessage.class, id);
    }

    @Override
    public List<FailQueueMessage> findAll() {
        final TypedQuery<FailQueueMessage> query = em.createQuery(SELECT_ALL_FAIL_QUEUE, FailQueueMessage.class);
        return query.getResultList();
    }
}
