package com.epam.cdp.router.controller;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.core.entity.TsException;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.XmlSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Geniy00
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private XmlSerializer xmlSerializer;

    @RequestMapping(value = "/execute")
    @ResponseBody
    public String execute(@RequestParam BookingRequestEnum.Action action, @RequestParam String orderId,
            @RequestParam Long bookingRequestId, @RequestParam(required = false) String reason) {
        try {
            //TODO: Can I just return a String without serailization?
            final BookingRequestEnum.Status status;
            switch (action) {
            case ACCEPT:
                status = orderService.acceptOrder(orderId, bookingRequestId);
                break;

            case REJECT:
                status = orderService.rejectOrder(orderId, bookingRequestId);
                break;

            case REFUSE:
                status = orderService.refuseOrder(orderId, bookingRequestId, reason);
                break;

            default:
                status = BookingRequestEnum.Status.FAILED;

            }
            return xmlSerializer.serialize(status);
        } catch (final TsException ex) {
            final String message = String.format("Can't execute an action %s for orderId=%s, bookingRequestId=%s",
                    action, orderId, bookingRequestId);
            LOG.error(message, ex);
            return message;
        }
    }

    @RequestMapping(value = "/getCustomerInfo")
    @ResponseBody
    public String getCustomerInfo(@RequestParam String orderId, @RequestParam Long bookingRequestId) {
        try {
            final Customer customer = orderService.getCustomerInfo(orderId, bookingRequestId);
            return xmlSerializer.serialize(customer);
        } catch (final TsException ex) {
            final String message = String.format("Can't get customer info for orderId=%s, bookingRequestId=%s",
                    orderId, bookingRequestId);
            LOG.error(message, ex);
            return message;
        }
    }


}
