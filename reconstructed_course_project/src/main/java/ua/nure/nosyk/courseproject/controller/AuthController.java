package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.form.*;
import ua.nure.nosyk.courseproject.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    @GetMapping("/login")
    public String login(Model m) {
        m.addAttribute("loginForm", new LoginForm());
        return "loginPage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm f, Model m, HttpSession s) throws Exception {
        User u = users.authenticate(f.getEmail(), f.getPassword());
        if (u == null) {
            m.addAttribute("errorMessage", "Invalid email or password");
            return "loginPage";
        }
        boolean banned = "BANNED".equalsIgnoreCase(u.getAccountStatus());
        u.setPassword(null);
        s.setAttribute("currentUser", u);

        if (banned) {
            return "redirect:/banned";
        }

        return "redirect:/advertisements";
    }

    @GetMapping("/register")
    public String register(Model m) {
        m.addAttribute("registrationForm", new RegistrationForm());
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegistrationForm f, Model m) throws Exception {
        if (f.getEmail() == null || f.getEmail().trim().isEmpty() || f.getPassword() == null || f.getPassword().isEmpty()) {
            m.addAttribute("errorMessage", "Email and password are required");
            return "registerPage";
        }
        if (users.authenticate(f.getEmail(), f.getPassword()) != null || users.findByEmail(f.getEmail()) != null) {
            m.addAttribute("errorMessage", "A user with this email already exists");
            return "registerPage";
        }
        User u = new User();
        u.setRole("ROLE_AUTHORIZED");
        u.setName(f.getName());
        u.setEmail(f.getEmail());
        u.setPassword(f.getPassword());
        u.setPhone(f.getPhone());
        u.setPassport(f.getPassport());
        u.setAddress(f.getAddress());
        u.setRating(0);
        users.register(u);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/advertisements";
    }
}