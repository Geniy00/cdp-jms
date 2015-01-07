package com.epam.cdp.sender.service;

import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.SourceSystem;
import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.sender.bean.RequestStorageBean;
import com.epam.cdp.sender.gateway.SenderGateway;
import com.google.common.base.Optional;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: check if Apache Camel can be used
@Component
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class);

    private static final String TIME_PATTERN = "dd-MMM-yyyy, HH:mm";

    private final Random random = new Random();

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private SenderGateway senderGateway;

    @Autowired
    private SourceSystem sourceSystem;

    @Autowired
    private RequestStorageBean storageBean;

    @Override
    public Long priceRequest(final ReservationRequest reservationRequest) {
        final Long requestId = generateRequestId();
        reservationRequest.setRequestId(requestId);
        reservationRequest.setIndicative(true);
        reservationRequest.setStatus(ReservationRequest.Status.DRAFT);

        sendToJms(reservationRequest);
        storageBean.newReservationRequest(reservationRequest);
        return requestId;
    }

    @Override
    public void orderRequest(final Long requestId) {
        final ReservationRequest reservationRequest = getRequestByIdWithRetrying(requestId);
        if (reservationRequest == null) {
            LOG.error(String.format(
                    "Can't order ReservationRequest with id=%s, because ReservationResponse wasn't received",
                    requestId));
            return;
        }
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

    private Long generateRequestId() {
        final long random = this.random.nextLong();
        return random > 0 ? random : -random;
    }

    @CheckForNull
    private ReservationRequest getRequestByIdWithRetrying(final Long requestId) {
        try {
            return executorService.submit(new AsyncOrderRequestSender(requestId)).get();
        } catch (final InterruptedException ex) {
            LOG.error(ex);
            return null;
        } catch (final ExecutionException ex) {
            LOG.error(ex);
            return null;
        }
    }

    private void sendToJms(final ReservationRequest reservationRequest) {
        reservationRequest.setSourceSystem(sourceSystem);
        senderGateway.send(reservationRequest);

        final String deliveryTime = reservationRequest.getDeliveryTime().toString(TIME_PATTERN);
        LOG.debug(String.format("ReservationRequest[deliveryTime: %s, from %s to %s] was sent", deliveryTime,
                reservationRequest.getStartPosition(), reservationRequest.getFinishPosition()));
    }

    private class AsyncOrderRequestSender implements Callable<ReservationRequest>{

        private static final int RETRYING_SLEEP = 3_000;
        private static final int TIMES_TO_RETRY = 10;

        private final Long requestId;

        private AsyncOrderRequestSender(final Long requestId) {
            this.requestId = requestId;
        }

        @Override
        public ReservationRequest call() throws Exception {
            for (int i = 0; i < TIMES_TO_RETRY; i++) {
                final ReservationRequest reservationRequest = getRequestById(requestId);
                if (isPriced(reservationRequest)) {
                    return reservationRequest;
                }
                try {
                    Thread.sleep(RETRYING_SLEEP);
                } catch (final InterruptedException ex) {
                    LOG.error(ex);
                }
            }
            throw new TsException(TsException.Reason.UNEXPECTED,
                    "ReservationRequest can't be ordered, because it wasn't priced yet");
        }

        private boolean isPriced(final ReservationRequest reservationRequest) {
            return reservationRequest != null && reservationRequest.getStatus() == ReservationRequest.Status.PRICED;
        }
    }
}
