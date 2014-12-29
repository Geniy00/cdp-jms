package com.epam.cdp.core.entity;

import java.io.Serializable;

/**
 * @author Geniy00
 */
public class ReservationResponse implements Serializable {

    private static final long serialVersionUID = 1823235672321505291L;

    private final Long requestId;

    private final Boolean isFailure;
    private final String failureReason;

    private final ReservationRequest.Status status;

    private final Double price;

    public ReservationResponse(final Long requestId, final Boolean isFailure, final String failureReason) {
        this.requestId = requestId;
        this.isFailure = isFailure;
        this.failureReason = failureReason;
        status = ReservationRequest.Status.FAILURE;
        this.price = 0.0;
    }

    public ReservationResponse(final Long requestId, final ReservationRequest.Status status, final Double price) {
        this.requestId = requestId;
        this.status = status;
        this.price = price;
        this.isFailure = false;
        this.failureReason = "";
    }

    public Long getRequestId() {
        return requestId;
    }

    public Boolean getIsFailure() {
        return isFailure;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public ReservationRequest.Status getStatus() {
        return status;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "requestId=" + requestId +
                ", isFailure=" + isFailure +
                ", price=" + price +
                '}';
    }
}
