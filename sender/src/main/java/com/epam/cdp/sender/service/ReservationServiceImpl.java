package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.SourceSystem;
import com.epam.cdp.sender.bean.RequestStorageBean;
import com.epam.cdp.sender.controller.TaxiReservationController;
import com.epam.cdp.sender.gateway.SenderGateway;
import com.google.common.base.Optional;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

//TODO: check if Apache Camel can be used
@Component
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class);

    private static final String TIME_PATTERN = "dd-MMM-yyyy, HH:mm";

    private final Random random = new Random();

    @Autowired
    private SenderGateway senderGateway;

    @Autowired
    private SourceSystem sourceSystem;

    @Autowired
    private RequestStorageBean storageBean;

    @Override
    public void sendToJms(final ReservationRequest reservationRequest) {
        reservationRequest.setSourceSystem(sourceSystem);
        senderGateway.send(reservationRequest);

        final String deliveryTime = reservationRequest.getDeliveryTime().toString(TIME_PATTERN);
        LOG.info(String.format("New ReservationRequest[deliveryTime: %s, from %s to %s] was sent", deliveryTime,
                reservationRequest.getStartPosition(), reservationRequest.getFinishPosition()));
    }

    @Override
    public Long generateRequestId() {
        final long random = this.random.nextLong();
        return random > 0 ? random : -random;
    }

    @Override
    public Long priceRequest(final ReservationRequest reservationRequest,
            TaxiReservationController taxiReservationController) {
        final Long requestId = generateRequestId();

        reservationRequest.setRequestId(requestId);
        reservationRequest.setIndicative(true);
        reservationRequest.setStatus(ReservationRequest.Status.DRAFT);
        final SourceSystem sourceSystem = new SourceSystem();
        reservationRequest.setSourceSystem(sourceSystem);
        sendToJms(reservationRequest);
        storageBean.newReservationRequest(reservationRequest);
        return requestId;
    }

    @Override
    public void orderRequest(final Long requestId) {
        final ReservationRequest reservationRequest = getRequestById(requestId);
        reservationRequest.setIndicative(false);
        sendToJms(reservationRequest);
    }

    @CheckForNull
    @Override
    public ReservationRequest getRequestById(final Long requestId) {
        final Optional<ReservationRequest> optionalReservationRequest = storageBean.getRequestById(requestId);
        return optionalReservationRequest.orNull();
    }

    @CheckForNull
    @Override
    public ReservationResponse getResponseById(final Long requestId) {
        final Optional<ReservationResponse> optionalReservationRequest = storageBean.getResponseById(requestId);
        return optionalReservationRequest.orNull();
    }

}
