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

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/history")
    public String showHistory(@RequestParam(required = false) Order.OrderStatus status, Model model) {
        List<Order> orders = null;
        if(status == null){
            orders = orderService.findAllOrders(400);
        } else {
            orders = orderService.findOrderByStatus(status, 400);
        }

        model.addAttribute("orders", orders);
        return "order/list";
    }

    @RequestMapping(value = "/order/{id}")
    public String showOrderDetails(@PathVariable String id,  Model model) {
        Order order = orderService.find(id);
        model.addAttribute("order", order);
        return "order/order";
    }

}