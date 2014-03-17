package ua.com.taxi.controller;

import com.epam.cdp.core.entity.BookingRequestEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.service.BookingService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/bookings")
    public String getRandomBooking(Model model) {
        Booking booking = bookingService.findFreeBooking();
        model.addAttribute("booking", booking);
        model.addAttribute("bookingCount", bookingService.countActualBookings());
        return "bookings";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.GET)
    public String getBookingById(@PathVariable Long id, Model model) {
        Booking booking = bookingService.find(id);
        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.POST)
    public String executeBookingAction(@PathVariable Long id,
                                        @RequestParam BookingRequestEnum.Action action,
                                        @RequestParam(required = false) String reason,
                                        Model model) {

        Booking booking = null;
        switch (action) {
            case ACCEPT:
                booking = bookingService.acceptBooking(id);
                break;

            case REJECT:
                booking = bookingService.rejectBooking(id);
                break;

            case REFUSE:
                if(reason == null) reason = "DEFAULT REFUSE REASON";
                booking = bookingService.refuseBooking(id, reason);
                break;
        }


        String message = "booking[id:" + booking.getId() + "] is " + booking.getStatus();

        model.addAttribute("booking", booking);
        model.addAttribute("message", message);
        return "bookingDetails";
    }

    @RequestMapping(value = "/booking/{id}/assigned")
    public String assignOrUnassignBooking(@PathVariable Long id,
                                          @RequestParam(required = false) String action,
                                          HttpServletRequest request,
                                          Model model) {

        Booking booking = null;
        switch (action == null ? "" : action) {
            case "ASSIGN_TO_ME":
                booking = bookingService.assignBooking(id);
                if(booking == null) {
                    return "redirect:/bookings";
                }
                break;

            case "UNASSIGN":
                booking = bookingService.unassignBooking(id);
                if(booking.getStatus() == Booking.BookingStatus.UNASSIGNED) {
                    return "redirect:/bookings";
                }
                break;
            case "":
                booking = bookingService.find(id);
                break;
        }

        if (action != null) {
            String message = "booking[id:" + booking.getId() + "] is " + action;
            model.addAttribute("message", message);
        }

        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

//    @RequestMapping(value = "/history")
//    public String showHistory(Model model) {
////        model.addAttribute("reportList", historyList.getReportHistory());
//        return "history";
//    }
}
