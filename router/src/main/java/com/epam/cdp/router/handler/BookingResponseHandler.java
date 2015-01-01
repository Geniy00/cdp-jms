package com.epam.cdp.router.handler;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.TsException;

/**
 * @author Geniy00
 */
public interface BookingResponseHandler {

    BookingRequestEnum.Status handleAcceptCommand(final String orderId, final Long bookingRequestId)
            throws TsException;

    BookingRequestEnum.Status handleRejectCommand(final String orderId, final Long bookingRequestId)
            throws TsException;

    BookingRequestEnum.Status handleRefuseCommand(final String orderId, final Long bookingRequestId,
            final String reason) throws TsException;

    Customer getCustomerInfo(final String orderId, final Long bookingRequestId) throws TsException;
}
