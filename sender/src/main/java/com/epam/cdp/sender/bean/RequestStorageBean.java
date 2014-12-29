package com.epam.cdp.sender.bean;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is used instead of database. It saves the current ReservationRequest and the last ReservationResponse
 *
 * @author Geniy00
 */
@Component
@ThreadSafe
public class RequestStorageBean {

    private static final Logger LOG = Logger.getLogger(RequestStorageBean.class);

    private static final long MAX_CACHE_SIZE = 10000;
    private static final int ACCESS_EXPIRATION_MINS = 30;
    private static final int WRITE_EXPIRATION_HOURS = 1;

    @GuardedBy("lock")
    private final Cache<Long, ReservationRequest> reservationRequestCache = CacheBuilder.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    @GuardedBy("lock")
    private final Cache<Long, ReservationResponse> reservationResponseCache = CacheBuilder.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterAccess(ACCESS_EXPIRATION_MINS, TimeUnit.MINUTES)
            .expireAfterWrite(WRITE_EXPIRATION_HOURS, TimeUnit.HOURS)
            .build();

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public Optional<ReservationRequest> getRequestById(final long requestId) {
        readLock.lock();
        try {
            final ReservationRequest reservationRequest = reservationRequestCache.getIfPresent(requestId);
            return Optional.fromNullable(reservationRequest);
        } finally {
            readLock.unlock();
        }
    }

    public Optional<ReservationResponse> getResponseById(final long requestId) {
        readLock.lock();
        try {
            final ReservationResponse reservationResponse = reservationResponseCache.getIfPresent(requestId);
            return Optional.fromNullable(reservationResponse);
        } finally {
            readLock.unlock();
        }
    }

    public void newReservationRequest(final ReservationRequest reservationRequest) {
        writeLock.lock();
        try {
            reservationRequestCache.put(reservationRequest.getRequestId(), reservationRequest);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean update(final ReservationResponse reservationResponse) {
        final Long requestId = reservationResponse.getRequestId();
        writeLock.lock();
        try {
            final ReservationRequest reservationRequest = reservationRequestCache.getIfPresent(requestId);
            if (reservationRequest == null) {
                LOG.error("Can't find a ReservationRequest by requestId " + requestId);
                return false;
            }
            reservationRequest.setStatus(reservationResponse.getStatus());

            setPriceIfNull(reservationRequest, reservationResponse.getPrice());

            reservationRequestCache.put(requestId, reservationRequest);
            reservationResponseCache.put(requestId, reservationResponse);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    private void setPriceIfNull(final ReservationRequest reservationRequest, final Double price) {
        if (reservationRequest.getPrice() == null) {
            reservationRequest.setPrice(price);
        }
    }
}
