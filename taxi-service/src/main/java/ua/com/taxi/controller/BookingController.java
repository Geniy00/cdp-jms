package ua.com.taxi.controller;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.TsException;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

import java.util.List;

@Controller
public class BookingController {

    private static final Logger LOG = Logger.getLogger(BookingController.class);

    private static final String DEFAULT_REFUSE_REASON = "DEFAULT REFUSE REASON";
    private static final int BOOKINGS_COUNT_LIMIT = 100;

    @Autowired
    BookingService bookingService;

    private enum AssignRevokeCommand {
        ASSIGN_TO_ME("ASSIGN_TO_ME"),
        REVOKE("REVOKE");

        private final String value;

        private AssignRevokeCommand(final String value) {
            this.value = value;
        }

        public static AssignRevokeCommand from(final String command) throws TsException {
            for (AssignRevokeCommand enumValue : AssignRevokeCommand.values()) {
                if (enumValue.name().equals(command)) {
                    return enumValue;
                }
            }
            throw new TsException(TsException.Reason.ACTION_IS_UNAVAILABLE, command);
        }
    }

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/bookings")
    public String getRandomBooking( @ModelAttribute("message") final String message, final Model model) {
        if(message.isEmpty()) {
            final Optional<Booking> bookingOptional = bookingService.findFreeBooking();
            if (bookingOptional.isPresent()) {
                model.addAttribute("booking", bookingOptional.get());
            }
            model.addAttribute("bookingCount", bookingService.countActualBookings());
        } else {
            model.addAttribute("message", message);
        }
        return "bookings";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.GET)
    public String getBookingById(@PathVariable final Long id, final Model model) {
        final Booking booking = bookingService.find(id);
        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.POST)
    public String executeBookingAction(@PathVariable final Long id,
            @RequestParam final BookingRequestEnum.Action action, @RequestParam(required = false) final String reason,
            final Model model) {

        final Booking booking;
        try {
            switch (action) {
            case ACCEPT:
                booking = bookingService.acceptBooking(id);
                break;

            case REJECT:
                bookingService.rejectBooking(id);
                return "redirect:/bookings";

            case REFUSE:
                final String refuseReason = (reason == null) ? DEFAULT_REFUSE_REASON : reason;
                booking = bookingService.refuseBooking(id, refuseReason);
                break;

            default:
                throw new TsException(TsException.Reason.ACTION_IS_UNAVAILABLE, action.name());
            }

            model.addAttribute("booking", booking);
        } catch (final TsException ex) {
            LOG.error(ex);
            final String failureMessage = String.format("Action %s can't be executed", action.name());
            model.addAttribute("message", failureMessage);
            model.addAttribute("booking", bookingService.find(id));
        }
        return "bookingDetails";
    }

    @RequestMapping(value = "/booking/{id}/assigned")
    public String assignOrRevokeBooking(@PathVariable final Long id,
            @RequestParam(required = false) final String action, final Model model,
            final RedirectAttributes redirectAttributes) {
        final Booking booking;
        try {
            final AssignRevokeCommand command = AssignRevokeCommand.from(action);
            switch (command) {
            case ASSIGN_TO_ME:
                booking = bookingService.assignBooking(id);
                break;

            case REVOKE:
                booking = bookingService.revokeBooking(id);
                if (booking.getStatus() == Booking.Status.REVOKED) {
                    return "redirect:/bookings";
                }
                break;
            default:
                throw new TsException(TsException.Reason.ACTION_IS_UNAVAILABLE, action);
            }

            model.addAttribute("booking", booking);
        } catch (final TsException ex) {
            LOG.error(ex);
            final String failureMessage = String.format("Action %s can't be executed", action);
            redirectAttributes.addAttribute("message", failureMessage);
            return "redirect:/bookings";
        }

        return "bookingDetails";
    }

    @RequestMapping(value = "/history")
    public String showGeneralHistory(Model model) {
        final List<Booking> bookings = bookingService.findBookings(BOOKINGS_COUNT_LIMIT);
        model.addAttribute("bookings", bookings);
        return "history";
    }

    @RequestMapping(value = "/filtered")
    public String showFilteredHistory(@RequestParam Booking.Status status, Model model) {
        List<Booking> bookings = bookingService.findBookingByStatus(status, BOOKINGS_COUNT_LIMIT);
        model.addAttribute("bookings", bookings);
        return "history";
    }
}
