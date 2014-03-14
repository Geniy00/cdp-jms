package com.epam.cdp.router.controller;

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

    @RequestMapping(value = "/index")
    @ResponseBody
    public String index() {
        return "Router service is turn on";
    }

    @RequestMapping(value = "/execute")
    @ResponseBody
    public String execute(@RequestParam String action,
                          @RequestParam Long id,
                          @RequestParam(required = false) String reason) {

        switch (action) {
            case "ACCEPT":
                Customer customer = orderService.acceptOrder(id);
                return xmlSerializer.serialize(customer);

            case "REJECT":
                orderService.rejectOrder(id);
                break;

            case "REFUSE":
                orderService.refuseOrder(id, reason);
                break;

            default:
                return "You can't execute [" + action + "] action.";

        }

        return "OK";
    }


}
