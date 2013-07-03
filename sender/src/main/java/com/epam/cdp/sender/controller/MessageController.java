package com.epam.cdp.sender.controller;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.Order.OrderType;
import com.epam.cdp.core.util.JsonGenerator;
import com.epam.cdp.core.util.OrderGenerator;
import com.epam.cdp.sender.bean.ScheduledOrderSender;
import com.epam.cdp.sender.bean.ScheduledOrderSender.Status;
import com.epam.cdp.sender.service.OrderService;
import com.google.gson.JsonSyntaxException;

@Controller
public class MessageController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	ScheduledOrderSender scheduledOrderSender;
	
	@RequestMapping(value="/")
	public String index(Model model){
		return "index";
	}
	
	@RequestMapping(value="/manual")
	public String manualSending(Model model){
		model.addAttribute("order", new Order());
		model.addAttribute("orderTypeValues", OrderType.values());
		return "manual";
	}
	
	@RequestMapping(value="/manual/random")
	public String randomManualSending(Model model, 
			@ModelAttribute("sentOrder") Order sentOrder){
		Order order = OrderGenerator.generateRandomOrder();		
		model.addAttribute("order", order);
		model.addAttribute("orderTypeValues", OrderType.values());
		model.addAttribute("sentOrder", sentOrder);
		return "manualRandom";
	}
	
	
	@RequestMapping(value="/send", method=RequestMethod.POST)
	public String sendOrder(@RequestHeader(value="referer") final String referer,
			@ModelAttribute Order order,
			final RedirectAttributes redirectAttributes){
		orderService.sendOrder(order);
		redirectAttributes.addFlashAttribute("sentOrder", order);
		return "redirect:" + referer;
	}
	
	@RequestMapping(value="/file")
	public String fileSending(Model model){
		return "file";
	}
	
	@RequestMapping(value="/file/send", method=RequestMethod.POST)
	public String uploadFile(@RequestParam(value = "file") MultipartFile file,
			Model model) throws IOException{
		
		if(file == null){
			model.addAttribute("error", "The file can't be received!");
			return "file";
		}
		
		StringWriter sw = new StringWriter();
		IOUtils.copy(file.getInputStream(), sw);
		String json = sw.toString();
		
		Order[] orders = null;
		try{
			orders = new JsonGenerator().parseJson(json);
		} catch (JsonSyntaxException e){
			model.addAttribute("error", "The file can't be parsed!");
			return "file";
		}
		
		for(Order order : orders){
			orderService.sendOrder(order);
		}
		
		String message = (orders == null || orders.length == 0) ? 
				"File can't be read" : orders.length + " orders were sent";
		
		model.addAttribute("message", message);
		
		return "file";
	}
	
	@RequestMapping(value="/automatic")
	public String automaticSending(Model model){
		model.addAttribute("status", scheduledOrderSender.getStatus());
		model.addAttribute("messageCount", scheduledOrderSender.getMessageCount());
		model.addAttribute("delay", scheduledOrderSender.getDelay());
		
		return "automatic";
	}
	
	@RequestMapping(value="/automatic/toggle", method=RequestMethod.POST)
	public String toggleSending(@RequestParam(value="delay", required=false)String delay, Model model){
		if(scheduledOrderSender.getStatus() == Status.STOPPED && delay == null){
			return "redirect:";
		}
		
		Long delayLong = null;		
		if(scheduledOrderSender.getStatus() == Status.STOPPED){
			try{
				delayLong = Long.valueOf(delay);
			} catch (NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		if(scheduledOrderSender.getStatus() == Status.STOPPED){
			scheduledOrderSender.startSending(delayLong);
		} else if (scheduledOrderSender.getStatus() == Status.SENDING){
			scheduledOrderSender.stopSending();
		}
		return "redirect:";
	}
}