package com.epam.cdp.sender.controller;

import com.epam.cdp.core.entity.Order;
import com.epam.cdp.core.entity.ReservationRequest;
import com.epam.cdp.core.entity.ReservationResponse;
import com.epam.cdp.core.entity.VehicleType;
import com.epam.cdp.sender.bean.ScheduledReservationRequestSender;
import com.epam.cdp.sender.service.ReservationService;
import com.epam.cdp.sender.util.JsonGenerator;
import com.epam.cdp.sender.util.ReservationRequestGenerator;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;

@Controller
public class TaxiReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ScheduledReservationRequestSender scheduledOrderSender;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/manual")
    public String manualSending(final Model model) {
        model.addAttribute("reservationRequest", new ReservationRequest());
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        return "manual";
    }

    @RequestMapping(value = "/manual/random")
    public String randomManualSending(final Model model, @ModelAttribute("sentOrder") final Order sentOrder) {
        ReservationRequest reservationRequest = ReservationRequestGenerator.generateRandomReservationRequest();
        model.addAttribute("reservationRequest", reservationRequest);
        model.addAttribute("vehicleTypeValues", VehicleType.values());
        return "manualRandom";
    }

    @RequestMapping(value = "/price", method = RequestMethod.POST)
    public String priceReservationRequest(@ModelAttribute final ReservationRequest reservationRequest) {
        final Long requestId = reservationService.priceRequest(reservationRequest, this);
        return "redirect:priced?requestId=" + requestId;
    }

    @RequestMapping(value = "/priced")
    public String displayPricedReservationRequest(@RequestParam final Long requestId, final Model model) {
        final ReservationRequest request = reservationService.getRequestById(requestId);
        model.addAttribute("reservationRequest", request);
        return "priced";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String orderReservationRequest(@ModelAttribute final ReservationRequest reservationRequest) {
        final Long requestId = reservationRequest.getRequestId();
        reservationService.orderRequest(requestId);
        return "redirect:ordered?requestId=" + requestId;
    }

    @RequestMapping(value = "/ordered")
    public String displayOrderedReservationRequest(@RequestParam final Long requestId, final Model model) {
        final ReservationRequest request = reservationService.getRequestById(requestId);
        model.addAttribute("reservationRequest", request);
        //TODO: remove
        model.addAttribute("reservationResponse", reservationService.getResponseById(requestId));
        return "ordered";
    }

    @ResponseBody
    @RequestMapping(value = "/getAjaxResponseObject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResponse ajaxGetRequestResponse(@RequestParam final Long requestId) {
        return reservationService.getResponseById(requestId);
    }

    @RequestMapping(value = "/file")
    public String fileSending() {
        return "file";
    }

    @RequestMapping(value = "/file/send", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(value = "file") final MultipartFile file, final Model model)
            throws IOException {

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
                reservationService.sendToJms(reservationRequest);
            }
            model.addAttribute("message", reservationRequests.length + " requests were sent");

        }
        return "file";
    }

    @RequestMapping(value = "/automatic")
    public String automaticSending(final Model model) {
        boolean isSending = scheduledOrderSender.isSending();
        model.addAttribute("status", isSending ? "SENDING" : "STOPPED");
        model.addAttribute("messageCount", scheduledOrderSender.getMessageCount());
        model.addAttribute("delay", scheduledOrderSender.getDelay());

        return "automatic";
    }

    @RequestMapping(value = "/automatic/toggle", method = RequestMethod.POST)
    public String toggleSending(@RequestParam(value = "delay", required = false) final Long delay) {
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