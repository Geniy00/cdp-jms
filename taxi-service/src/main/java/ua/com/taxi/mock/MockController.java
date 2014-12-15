package ua.com.taxi.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MockController {

    private static final int DEFAULT_DELAY = 1000;
    private static final int DEFAULT_ORDER_NUMBER_TO_BE_REJECTED = 5;

    @Autowired
    AutoReceiverBean autoReceiverBean;

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mock(Model model) {
        final String status = autoReceiverBean.isEnabled() ? "enabled" : "disabled";

        model.addAttribute("delay", DEFAULT_DELAY);
        model.addAttribute("rejectEveryNthOrder", DEFAULT_ORDER_NUMBER_TO_BE_REJECTED);
        model.addAttribute("status", status);
        return "mock";
    }


    @RequestMapping(value = "/mock", method = RequestMethod.POST)
    public String mockAction(@RequestParam String action, @RequestParam(required = false) Integer delay,
            @RequestParam(required = false) Integer rejectEveryNthOrder) {

        switch (ActionEnum.from(action)) {
        case ENABLE:
            autoReceiverBean.start(delay, rejectEveryNthOrder);
            break;
        case DISABLE:
            autoReceiverBean.stop();
            break;
        }
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
