package com.epam.cdp.management.controller;

import com.epam.cdp.core.entity.FailQueueMessage;
import com.epam.cdp.management.service.FailQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Geniy00
 */
@Controller
public class FailQueueController {

    @Autowired
    FailQueueService failQueueService;

    @RequestMapping("/failQueue")
    public String list(final Model model) {
        final List<FailQueueMessage> messages = failQueueService.findAll();
        model.addAttribute("messages", messages);
        return "failQueue/list";
    }

    @RequestMapping(value = "/failQueue/resend/{id}", method = RequestMethod.POST)
    public String resend(@PathVariable final Long id) {
        failQueueService.delete(id);
        return "redirect:/failQueue";
    }

    @RequestMapping(value = "/failQueue/remove/{id}", method = RequestMethod.POST)
    public String remove(@PathVariable final Long id) {
        failQueueService.delete(id);
        return "redirect:/failQueue";
    }

}
