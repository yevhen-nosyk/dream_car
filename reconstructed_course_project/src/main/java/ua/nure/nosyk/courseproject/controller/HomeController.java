package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import ua.nure.nosyk.courseproject.entity.User;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String home(HttpSession s) {
        User u = (User) s.getAttribute("currentUser");
        if (u != null
                && "BANNED".equalsIgnoreCase(u.getAccountStatus())) {
            return "redirect:/banned";
        }
        return "redirect:/advertisements";
    }

    @GetMapping("/banned")
    public String banned() {
        return "banned";
    }
}