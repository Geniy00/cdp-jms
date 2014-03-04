package com.epam.cdp.management.controller;

import com.epam.cdp.core.entity.BookingResponse.BookingResponseStatus;
import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Report;
import com.epam.cdp.management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    ReportService reportService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/reports")
    public String showReportList(Model model) {
        List<Report> reports = reportService.getAllReports();
        //findFromInterval(0, 100);
        model.addAttribute("reports", reports);
        return "report/list";
    }

    @RequestMapping("/report/{id}")
    public String showReport(@PathVariable String id, Model model) {
        Report report = reportService.find(id);
        model.addAttribute("report", report);
        return "report/view";
    }

    @RequestMapping("/order/{id}")
    public String showOrder(@PathVariable String id, Model model) {
        Report report = reportService.find(id);
        Order order = report.getOrder();
        model.addAttribute("order", order);
        return "order/view";
    }

    @RequestMapping("/error")
    public String showFailureReports(Model model) {
        List<Report> reports = reportService.findReportWithStatus(BookingResponseStatus.FAILURE);
        model.addAttribute("reports", reports);

        return "error/list";
    }

}
