package ua.com.taxi.controller;

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
    public String getRandomOrder(Model model) {
        Booking booking = bookingService.findFreeBooking();
        model.addAttribute("booking", booking);
        model.addAttribute("bookingCount", bookingService.countActualBookings());
        return "booking";
    }

    @RequestMapping(value = "/order/{id}/accept", method = RequestMethod.POST)
    public String acceptOrder(@PathVariable String id, Model model) {
//        Order order = bookingService.acceptBooking(id);
//        model.addAttribute("order", order);
//        model.addAttribute("queueSize", bookingService.getQueueSize());

        return "booking";
    }

    @RequestMapping(value = "/order/{id}/reject", method = RequestMethod.POST)
    public String rejectOrder(@PathVariable String id, Model model) {
//        bookingService.rejectBooking(id);

        return "redirect:/booking";
    }

    @RequestMapping(value = "/order/{id}/refuse", method = RequestMethod.POST)
    public String refuseOrder(@PathVariable String id,
                              @RequestParam(value = "reason") String reason, Model model) {
//        bookingService.refuseBooking(id, reason);

        return "redirect:/history";
    }

    @RequestMapping(value = "/history")
    public String showHistory(Model model) {
//        model.addAttribute("reportList", historyList.getReportHistory());
        return "history";
    }
}
