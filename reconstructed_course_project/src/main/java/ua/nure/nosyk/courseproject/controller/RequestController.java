package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.form.RequestForm;
import ua.nure.nosyk.courseproject.service.RequestService;

import javax.servlet.http.HttpSession;

@Controller
public class RequestController {
    private final RequestService requests;

    public RequestController(RequestService requests) {
        this.requests = requests;
    }

    private User user(HttpSession s) {
        return (User) s.getAttribute("currentUser");
    }

    @GetMapping("/requests")
    public String requests(Model m, HttpSession s) throws Exception {
        return sent(m, s);
    }

    @GetMapping("/requests/sent")
    public String sent(Model m, HttpSession s) throws Exception {
        User u = user(s);
        if (u == null) return "redirect:/login";
        m.addAttribute("requests", requests.sent(u.getId()));
        m.addAttribute("boxTitle", "My requests");
        return "requests";
    }

    @GetMapping("/requests/received")
    public String received(Model m, HttpSession s) throws Exception {
        User u = user(s);
        if (u == null) return "redirect:/login";
        m.addAttribute("requests", requests.received(u.getId()));
        m.addAttribute("boxTitle", "Incoming requests");
        return "requests";
    }

    @GetMapping("/request/new")
    public String form(@RequestParam Long receiverId, @RequestParam(required = false) Long carId, Model m, HttpSession s) {
        if (user(s) == null) return "redirect:/login";
        RequestForm f = new RequestForm();
        f.setReceiverId(receiverId);
        f.setProposedCarId(carId);
        m.addAttribute("requestForm", f);
        return "requestPage";
    }

    @PostMapping("/request/new")
    public String create(@ModelAttribute RequestForm f, HttpSession s) throws Exception {
        User u = user(s);
        if (u == null) return "redirect:/login";
        requests.create(u.getId(), f.getReceiverId(), f.getProposedCarId(), f.getDepositSum(), f.getPaymentMethod());
        return "redirect:/requests/sent";
    }

    @PostMapping("/requests/{id}/accept")
    public String accept(@PathVariable long id, HttpSession s) throws Exception {
        User u = user(s);
        if (u != null) {
            ua.nure.nosyk.courseproject.entity.Request r = requests.find(id);
            if (r != null && r.getReceiverId().equals(u.getId()) && "SENT".equals(r.getStatus())) requests.status(id, "ACCEPTED");
        }
        return "redirect:/requests/received";
    }

    @PostMapping("/requests/{id}/reject")
    public String reject(@PathVariable long id, HttpSession s) throws Exception {
        User u = user(s);
        if (u != null) {
            ua.nure.nosyk.courseproject.entity.Request r = requests.find(id);
            if (r != null && r.getReceiverId().equals(u.getId()) && "SENT".equals(r.getStatus())) requests.status(id, "REJECTED");
        }
        return "redirect:/requests/received";
    }
}