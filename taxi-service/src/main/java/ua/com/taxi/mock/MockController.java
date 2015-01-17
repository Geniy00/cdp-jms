package ua.com.taxi.mock;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MockController {

    private int defaultDelay = 500;
    private int defaultOrderNumberToBeRejected = 8;

    @Autowired
    AutoReceiverBean autoReceiverBean;

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mock(@RequestParam(required = false) Integer delay,
            @RequestParam(required = false) Integer rejectEveryNthOrder, Model model) {

        final String status;
        if(autoReceiverBean.isEnabled()) {
            status = "enabled";
            delay = autoReceiverBean.getDelayBetweenReceiving();
            rejectEveryNthOrder = autoReceiverBean.getRejectEveryNthOrder();
        } else {
            status = "disabled";
            delay = Optional.fromNullable(delay).or(defaultDelay);
            rejectEveryNthOrder =Optional.fromNullable(rejectEveryNthOrder).or(defaultOrderNumberToBeRejected);
        }

        model.addAttribute("delay", delay);
        model.addAttribute("rejectEveryNthOrder", rejectEveryNthOrder);
        model.addAttribute("status", status);
        return "mock";
    }


    @RequestMapping(value = "/mock", method = RequestMethod.POST)
    public String mockAction(@RequestParam String action, @RequestParam(required = false) Integer delay,
            @RequestParam(required = false) Integer rejectEveryNthOrder) {

        switch (ActionEnum.from(action)) {
        case ENABLE:
            autoReceiverBean.start(delay, rejectEveryNthOrder);
            defaultDelay = delay;
            defaultOrderNumberToBeRejected = rejectEveryNthOrder;
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
