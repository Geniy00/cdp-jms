package com.epam.cdp.management.controller;

import com.epam.cdp.core.entity.TaxiDispatcher;
import com.epam.cdp.management.service.TaxiDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Geniy00
 */
@Controller
public class TaxiDispatcherController {

    @Autowired
    private TaxiDispatcherService taxiDispatcherService;

    @RequestMapping("/dispatchers")
    public String list(final Model model) {
        final List<TaxiDispatcher> dispatchers = taxiDispatcherService.findAll();
        model.addAttribute("dispatchers", dispatchers);
        return "dispatcher/list";
    }

    @RequestMapping(value = "/dispatcher/new", method = RequestMethod.GET)
    public String newTaxiDispatcher(final Model model) {
        final TaxiDispatcher dispatcher = new TaxiDispatcher();
        model.addAttribute("action", "Create");
        model.addAttribute("dispatcher", dispatcher);
        return "dispatcher/new";
    }

    @RequestMapping(value = "/dispatcher/new", method = RequestMethod.POST)
    public String processNewTaxiDispatcher(@ModelAttribute("dispatcher") final TaxiDispatcher taxiDispatcher) {
        final TaxiDispatcher dispatcher = taxiDispatcherService.update(taxiDispatcher);
        return "redirect:/dispatcher/show/" + dispatcher.getId();
    }

    @RequestMapping("/dispatcher/show/{id}")
    public String showTaxiDispatcher(@PathVariable final Long id, final Model model) {
        final TaxiDispatcher dispatcher = taxiDispatcherService.find(id);
        model.addAttribute("dispatcher", dispatcher);
        return "dispatcher/dispatcher";
    }

    @RequestMapping(value = "/dispatcher/edit/{id}", method = RequestMethod.GET)
    public String editTaxiDispatcher(@PathVariable final Long id, final Model model) {
        final TaxiDispatcher dispatcher = taxiDispatcherService.find(id);
        model.addAttribute("action", "Update");
        model.addAttribute("dispatcher", dispatcher);
        return "dispatcher/new";
    }

    @RequestMapping(value = "/dispatcher/edit/{id}", method = RequestMethod.POST)
    public String processEditTaxiDispatcher(@PathVariable final Long id,
            @ModelAttribute("dispatcher") final TaxiDispatcher taxiDispatcher) {
        taxiDispatcherService.update(taxiDispatcher);
        return "redirect:/dispatcher/show/" + id;
    }

    @RequestMapping(value = "/dispatcher/remove/{id}", method = RequestMethod.POST)
    public String removeTaxiDispatcher(@PathVariable final Long id) {
        taxiDispatcherService.delete(id);
        return "redirect:/dispatchers";
    }

}
