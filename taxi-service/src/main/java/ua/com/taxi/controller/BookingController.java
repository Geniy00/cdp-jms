package ua.com.taxi.controller;

import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.TsException;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/bookings")
    public String getRandomBooking(final Model model) {
        final Optional<Booking> bookingOptional = bookingService.findFreeBooking();
        if (bookingOptional.isPresent()) {
            model.addAttribute("booking", bookingOptional.get());
        }
        model.addAttribute("bookingCount", bookingService.countActualBookings());
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
            @RequestParam(required = false) final String action, final Model model) {

        final Booking booking;
        try {
            switch (action) {
            case "ASSIGN_TO_ME":
                booking = bookingService.assignBooking(id);
                if (booking == null) {
                    return "redirect:/bookings";
                }
                break;

            case "REVOKE":
                booking = bookingService.revokeBooking(id);
                if (booking.getStatus() == Booking.BookingStatus.REVOKED) {
                    return "redirect:/bookings";
                }
                break;
            default:
                throw new TsException(TsException.Reason.ACTION_IS_UNAVAILABLE, action);
            }

            model.addAttribute("booking", booking);
            final String message = String.format("booking[id: %s] is %s", booking.getId(), action);
            model.addAttribute("message", message);
        } catch (final TsException ex) {
            LOG.error(ex);
            final String failureMessage = String.format("Action %s can't be executed", action);
            model.addAttribute("message", failureMessage);
            model.addAttribute("booking", bookingService.find(id));
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
    public String showFilteredHistory(@RequestParam Booking.BookingStatus status, Model model) {
        List<Booking> bookings = bookingService.findBookingByStatus(status, BOOKINGS_COUNT_LIMIT);
        model.addAttribute("bookings", bookings);
        return "history";
    }
}
