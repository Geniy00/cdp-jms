package ua.com.taxi.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MockController {

    private static final int DEFAULT_DELAY = 500;
    private static final int DEFAULT_ORDER_NUMBER_TO_BE_REJECTED = 8;

    @Autowired
    AutoReceiverBean autoReceiverBean;

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mock(@RequestParam(required = false) Integer delay,
            @RequestParam(required = false) Integer rejectEveryNthOrder, Model model) {
        final String status = autoReceiverBean.isEnabled() ? "enabled" : "disabled";

        model.addAttribute("delay", delay == null ? DEFAULT_DELAY : delay);
        model.addAttribute("rejectEveryNthOrder",
                rejectEveryNthOrder == null ? DEFAULT_ORDER_NUMBER_TO_BE_REJECTED : rejectEveryNthOrder);
        model.addAttribute("status", status);
        return "mock";
    }


    @RequestMapping(value = "/mock", method = RequestMethod.POST)
    public String mockAction(@RequestParam String action, @RequestParam(required = false) Integer delay,
            @RequestParam(required = false) Integer rejectEveryNthOrder, final RedirectAttributes redirectAttributes) {

        switch (ActionEnum.from(action)) {
        case ENABLE:
            autoReceiverBean.start(delay, rejectEveryNthOrder);
            break;
        case DISABLE:
            autoReceiverBean.stop();
            break;
        }
        redirectAttributes.addAttribute("delay", delay);
        redirectAttributes.addAttribute("rejectEveryNthOrder", rejectEveryNthOrder);
        return "redirect:/mock";
    }

    private enum ActionEnum {
        ENABLE("Enable"),
        DISABLE("Disable");

        private final String value;

        ActionEnum(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ActionEnum from(final String stringValue) {
            for (ActionEnum item : values()) {
                final String itemStringValue = item.getValue();
                if (itemStringValue.equals(stringValue)) {
                    return item;
                }
            }
            //TODO: use custom exception
            throw new RuntimeException("Action can't be executed");
        }
    }
}
