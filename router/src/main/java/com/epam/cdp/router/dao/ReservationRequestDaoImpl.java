package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.ReservationRequest;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Geniy00
 */
@Repository
@Transactional
public class ReservationRequestDaoImpl implements ReservationRequestDao {

    private static final String SELECT_REQUEST_BY_REQUEST_ID = "SELECT req FROM ReservationRequest req " +
            "WHERE req.requestId = :requestId";

    private static final String SELECT_EXPIRED_PRICED_REQUESTS = "SELECT req FROM ReservationRequest req " +
            "WHERE req.indicative = true AND req.createdTimestamp > :expirationTime";

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReservationRequest saveOrUpdate(final ReservationRequest reservationRequest) {
        return em.merge(reservationRequest);
    }

    @Override
    public void delete(final Long id) {
        em.remove(em.find(ReservationRequest.class, id));
    }

    @Override
    public ReservationRequest findByRequestId(final Long requestId) {
        final TypedQuery<ReservationRequest> query = em.createQuery(SELECT_REQUEST_BY_REQUEST_ID,
                ReservationRequest.class);
        query.setParameter("requestId", requestId);
        return query.getSingleResult();
    }

    @Override
    public List<ReservationRequest> findExpiredPricedRequests(final DateTime expirationTime) {
        final TypedQuery<ReservationRequest> query = em.createQuery(SELECT_EXPIRED_PRICED_REQUESTS,
                ReservationRequest.class);
        query.setParameter("expirationTime", expirationTime);
        return query.getResultList();
    }
}
