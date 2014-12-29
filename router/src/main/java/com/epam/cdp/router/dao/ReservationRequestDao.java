package com.epam.cdp.router.dao;

import com.epam.cdp.core.entity.ReservationRequest;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Geniy00
 */
public interface ReservationRequestDao {

    ReservationRequest saveOrUpdate(ReservationRequest reservationRequest);

    void delete(Long id);

    ReservationRequest findByRequestId(Long requestId);

    List<ReservationRequest> findExpiredPricedRequests(DateTime expirationTime);
}
