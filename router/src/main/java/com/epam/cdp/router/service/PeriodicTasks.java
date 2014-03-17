package com.epam.cdp.router.service;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.BookingResponse;
import com.epam.cdp.router.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Geniy00
 */
@Component
public class PeriodicTasks {

    @Autowired
    OrderDao orderDao;

    //TODO: check if this task works good
    public void  handleExpiredOrdersWithoutResponse(){
        List<BookingRequest> expiredBookingRequests = orderDao.findExpiredBookingRequests();
        for (BookingRequest bookingRequest : expiredBookingRequests) {
            BookingResponse bookingResponse = new BookingResponse(bookingRequest, BookingRequestEnum.Status.EXPIRED);
            bookingRequest.applyBookingResponse(bookingResponse);
            orderDao.updateBookingRequest(bookingRequest);
        }

    }

}
