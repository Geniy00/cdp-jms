package com.epam.cdp.sender.controller;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.VehicleType;
import com.epam.cdp.core.util.JsonGenerator;
import com.epam.cdp.core.util.ReservationRequestGenerator;
import com.epam.cdp.sender.bean.ScheduledReservationRequestSender;
import com.epam.cdp.sender.bean.ScheduledReservationRequestSender.Status;
import com.epam.cdp.sender.service.ReservationService;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.StringWriter;

@Controller
public class MessageController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ScheduledReservationRequestSender scheduledOrderSender;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/manual")
    public String manualSending(Model model) {
        model.addAttribute("reservationRequest", new ReservationRequest());
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        return "manual";
    }

    @RequestMapping(value = "/manual/random")
    public String randomManualSending(Model model,
                                      @ModelAttribute("sentOrder") Order sentOrder) {
        ReservationRequest reservationRequest = ReservationRequestGenerator.generateRandomReservationRequest();
        model.addAttribute("reservationRequest", reservationRequest);
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        model.addAttribute("sentOrder", sentOrder);
        return "manualRandom";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendReservationRequest(@RequestHeader(value = "referer") final String referer,
                                         @ModelAttribute ReservationRequest reservationRequest,
                                         final RedirectAttributes redirectAttributes) {
        reservationService.sendReservationRequest(reservationRequest);
        redirectAttributes.addFlashAttribute("sentReservationRequest", reservationRequest);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/file")
    public String fileSending(Model model) {
        return "file";
    }

    @RequestMapping(value = "/file/send", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(value = "file") MultipartFile file,
                             Model model) throws IOException {

        if (file == null) {
            model.addAttribute("error", "The file can't be received!");
            return "file";
        }

        StringWriter sw = new StringWriter();
        IOUtils.copy(file.getInputStream(), sw);
        String json = sw.toString();

        ReservationRequest[] reservationRequests = null;
        try {
            reservationRequests = new JsonGenerator().parseJson(json);
        } catch (JsonSyntaxException e) {
            model.addAttribute("error", "The file can't be parsed!");
            return "file";
        }

        for (ReservationRequest reservationRequest : reservationRequests) {
            reservationService.sendReservationRequest(reservationRequest);
        }

        String message = (reservationRequests == null || reservationRequests.length == 0) ?
                "File can't be read" : reservationRequests.length + " requests were sent";

        model.addAttribute("message", message);

        return "file";
    }

    @RequestMapping(value = "/automatic")
    public String automaticSending(Model model) {
        model.addAttribute("status", scheduledOrderSender.getStatus());
        model.addAttribute("messageCount", scheduledOrderSender.getMessageCount());
        model.addAttribute("delay", scheduledOrderSender.getDelay());

        return "automatic";
    }

    @RequestMapping(value = "/automatic/toggle", method = RequestMethod.POST)
    public String toggleSending(@RequestParam(value = "delay", required = false) String delay, Model model) {
        if (scheduledOrderSender.getStatus() == Status.STOPPED && delay == null) {
            return "redirect:";
        }

        Long delayLong = null;
        if (scheduledOrderSender.getStatus() == Status.STOPPED) {
            try {
                delayLong = Long.valueOf(delay);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (scheduledOrderSender.getStatus() == Status.STOPPED) {
            scheduledOrderSender.startSending(delayLong);
        } else if (scheduledOrderSender.getStatus() == Status.SENDING) {
            scheduledOrderSender.stopSending();
        }
        return "redirect:";
    }
}