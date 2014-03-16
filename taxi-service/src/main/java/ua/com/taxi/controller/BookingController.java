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

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/booking")
    public String getRandomBooking(Model model) {
        Booking booking = bookingService.findFreeBooking();
        model.addAttribute("booking", booking);
        model.addAttribute("bookingCount", bookingService.countActualBookings());
        return "booking";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.GET)
    public String getBookingById(@PathVariable Long id, Model model) {
        Booking booking = bookingService.find(id);
        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

    @RequestMapping(value = "/booking/{id}", method = RequestMethod.POST)
    public String acceptOrRejectBooking(@PathVariable Long id,
                                        @RequestParam BookingRequestEnum.Action action,
                                        Model model) {

        Booking booking = null;
        switch (action) {
            case ACCEPT:
                booking = bookingService.acceptBooking(id);
                break;

            case REJECT:
                booking = bookingService.rejectBooking(id);
                break;
        }


        String message = "booking[id:" + booking.getId() + "] is " + booking.getStatus();

        model.addAttribute("booking", booking);
        model.addAttribute("message", message);
        return "bookingDetails";
    }

//    @RequestMapping(value = "/booking/{id}/refuse", method = RequestMethod.POST)
//    public String refuseBooking(@PathVariable String id,
//                              @RequestParam(value = "reason") String reason, Model model) {
////        bookingService.refuseBooking(id, reason);
//
//        return "redirect:/history";
//    }
//
//    @RequestMapping(value = "/history")
//    public String showHistory(Model model) {
////        model.addAttribute("reportList", historyList.getReportHistory());
//        return "history";
//    }
}
