package com.epam.cdp.router.controller;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.Customer;
import com.epam.cdp.router.service.OrderService;
import com.epam.cdp.router.service.XmlSerializer;
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

    @Autowired
    OrderService orderService;

    @Autowired
    XmlSerializer xmlSerializer;

    @RequestMapping(value = "/execute")
    @ResponseBody
    public String execute(@RequestParam BookingRequestEnum.Action action,
                          @RequestParam String orderId,
                          @RequestParam Long bookingRequestId,
                          @RequestParam(required = false) String reason) {

        switch (action) {
            case ACCEPT:
                /*
                TODO: May we need to split acceptance request onto two:
                - accept (get error code in case of expired, wrong ids, ..)
                - getCustomer info (get customer info by some tokens)
                */
                Customer customer = orderService.acceptOrder(orderId, bookingRequestId);
                return xmlSerializer.serialize(customer);

            case REJECT:
                BookingRequestEnum.Status status = orderService.rejectOrder(orderId, bookingRequestId);
                return xmlSerializer.serialize(status);

            case REFUSE:
                //TODO: why should we use status1?
                BookingRequestEnum.Status status1 = orderService.refuseOrder(orderId, bookingRequestId, reason);
                return xmlSerializer.serialize(status1);

            default:
                return "You can't execute [" + action + "] action.";

        }
    }


}
