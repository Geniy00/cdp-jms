package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.cdp.core.entity.Order;

import ua.com.taxi.bean.HistoryList;
import ua.com.taxi.service.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	HistoryList historyList;
	
	@RequestMapping(value="/")
	public String index(Model model){
		return "index";
	}
	
	@RequestMapping(value="/order")
	public String getRandomOrder(Model model){
		Order order = orderService.peekOrder();
		model.addAttribute("order", order);
		model.addAttribute("queueSize", orderService.getQueueSize());
		return "order";
	}		
	
	@RequestMapping(value="/order/{id}/accept", method=RequestMethod.POST)
	public String acceptOrder(@PathVariable String id, Model model){
		Order order = orderService.acceptOrder(id);
		model.addAttribute("order", order);
		model.addAttribute("queueSize", orderService.getQueueSize());
		
		return "order";
	}
	
	@RequestMapping(value="/order/{id}/reject", method=RequestMethod.POST)
	public String rejectOrder(@PathVariable String id, Model model){
		orderService.rejectOrder(id);

		return "redirect:/order";
	}
	
	@RequestMapping(value="/order/{id}/refuse", method=RequestMethod.POST)
	public String refuseOrder(@PathVariable String id, 
			@RequestParam(value="reason") String reason, Model model){
		orderService.refuseOrder(id, reason);

		return "redirect:/history";
	}
	
	@RequestMapping(value="/history")
	public String showHistory(Model model){
		model.addAttribute("reportList", historyList.getReportHistory());
		return "history";
	}
}
