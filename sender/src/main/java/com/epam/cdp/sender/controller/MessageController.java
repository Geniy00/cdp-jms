package com.epam.cdp.sender.controller;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.VehicleType;
import com.epam.cdp.sender.bean.ScheduledReservationRequestSender;
import com.epam.cdp.sender.service.ReservationService;
import com.epam.cdp.sender.util.JsonGenerator;
import com.epam.cdp.sender.util.ReservationRequestGenerator;
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
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/manual")
    public String manualSending(Model model) {
        model.addAttribute("reservationRequest", new ReservationRequest());
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        return "manual";
    }

    @RequestMapping(value = "/manual/random")
    public String randomManualSending(Model model, @ModelAttribute("sentOrder") Order sentOrder) {
        ReservationRequest reservationRequest = ReservationRequestGenerator.generateRandomReservationRequest();
        model.addAttribute("reservationRequest", reservationRequest);
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        model.addAttribute("sentOrder", sentOrder);
        return "manualRandom";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendReservationRequest(@RequestHeader(value = "referrer") final String referrer,
            @ModelAttribute ReservationRequest reservationRequest, final RedirectAttributes redirectAttributes) {
        reservationService.sendReservationRequest(reservationRequest);
        redirectAttributes.addFlashAttribute("sentReservationRequest", reservationRequest);
        return "redirect:" + referrer;
    }

    @RequestMapping(value = "/file")
    public String fileSending() {
        return "file";
    }

    @RequestMapping(value = "/file/send", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(value = "file") MultipartFile file, Model model) throws IOException {

        if (file == null) {
            model.addAttribute("error", "The file can't be received!");
            return "file";
        }

        final StringWriter stringWriter = new StringWriter();
        IOUtils.copy(file.getInputStream(), stringWriter);
        final String json = stringWriter.toString();

        final ReservationRequest[] reservationRequests;
        try {
            reservationRequests = new JsonGenerator().parseJson(json);
        } catch (JsonSyntaxException e) {
            model.addAttribute("error", "The file can't be parsed!");
            return "file";
        }

        if (reservationRequests == null || reservationRequests.length == 0) {
            model.addAttribute("message", "File can't be read");
        } else {
            for (ReservationRequest reservationRequest : reservationRequests) {
                reservationService.sendReservationRequest(reservationRequest);
            }
            model.addAttribute("message", reservationRequests.length + " requests were sent");

        }
        return "file";
    }

    @RequestMapping(value = "/automatic")
    public String automaticSending(Model model) {
        boolean isSending = scheduledOrderSender.isSending();
        model.addAttribute("status", isSending ? "SENDING" : "STOPPED");
        model.addAttribute("messageCount", scheduledOrderSender.getMessageCount());
        model.addAttribute("delay", scheduledOrderSender.getDelay());

        return "automatic";
    }

    @RequestMapping(value = "/automatic/toggle", method = RequestMethod.POST)
    public String toggleSending(@RequestParam(value = "delay", required = false) Long delay) {
        if (!scheduledOrderSender.isSending() && delay == null) {
            return "redirect:";
        }

        if (scheduledOrderSender.isSending()) {
            scheduledOrderSender.stopSending();
        } else {
            scheduledOrderSender.startSending(delay);
        }
        return "redirect:";
    }
}