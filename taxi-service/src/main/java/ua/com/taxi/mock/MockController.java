package ua.com.taxi.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MockController {

    @Autowired
    AutoReceiverBean autoReceiverBean;

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mock(Model model) {
        String status = autoReceiverBean.isEnabled() ? "enabled" : "disabled";
        String action = autoReceiverBean.isEnabled() ? "Disable" : "Enable";

        model.addAttribute("delay", 1000);
        model.addAttribute("rejectEveryNthOrder", 10);
        model.addAttribute("status", status);
        model.addAttribute("action", action);
        return "mock";
    }


    @RequestMapping(value = "/mock", method = RequestMethod.POST)
    public String mockAction(@RequestParam String action,
                             @RequestParam(required = false) int delay,
                             @RequestParam(required = false) int rejectEveryNthOrder,
                             Model model) {

        if ("Enable".equals(action)) {
            autoReceiverBean.start(delay, rejectEveryNthOrder);
        } else if ("Disable".equals(action)) {
            autoReceiverBean.stop();
        }

        return "redirect:/mock";
    }
}