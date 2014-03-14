package ua.com.taxi.controller;

import com.epam.cdp.core.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.taxi.dao.BookingDao;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.ClientDetails;
import ua.com.taxi.service.BookingService;

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;
    
    //TODO remove it
    @Autowired
    BookingDao bookingDao;

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

    @RequestMapping(value = "/booking/{id}/accept", method = RequestMethod.POST)
    public String acceptOrder(@PathVariable Long id, Model model) {
        Booking booking = bookingService.acceptBooking(id);

        model.addAttribute("booking", booking);
        model.addAttribute("bookingCount", bookingService.countActualBookings());
        return "booking";
//        ClientDetails clientDetails = new ClientDetails();
//        clientDetails.setName(customer.getName());
//        clientDetails.setPhone(customer.getPhone());
//        booking.setClient(clientDetails);
//        model.addAttribute("booking", booking);
//        model.addAttribute("bookingCount", bookingService.countActualBookings());
//        return "booking";
    }

    @RequestMapping(value = "/booking/{id}/reject", method = RequestMethod.POST)
    public String rejectOrder(@PathVariable String id, Model model) {
//        bookingService.rejectBooking(id);

        return "redirect:/booking";
    }

    @RequestMapping(value = "/booking/{id}/refuse", method = RequestMethod.POST)
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
