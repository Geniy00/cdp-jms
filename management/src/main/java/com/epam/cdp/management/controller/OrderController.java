package com.epam.cdp.management.controller;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Geniy00
 */
@Controller
public class OrderController {

    private static final int ORDER_COUNT_LIMIT = 400;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/history")
    public String showHistory(@RequestParam(required = false) final Order.OrderStatus status, final Model model) {
        final List<Order> orders;
        if (status == null) {
            orders = orderService.findAllOrders(ORDER_COUNT_LIMIT);
        } else {
            orders = orderService.findOrderByStatus(status, ORDER_COUNT_LIMIT);
        }

        model.addAttribute("orders", orders);
        return "order/list";
    }

    @RequestMapping(value = "/order/{id}")
    public String showOrderDetails(@PathVariable final String id, final Model model) {
        final Order order = orderService.find(id);
        model.addAttribute("order", order);
        return "order/order";
    }

}
