package ua.nure.nosyk.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.service.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService users;
    private final AdminService admin;

    public AdminController(UserService users, AdminService admin) {
        this.users = users;
        this.admin = admin;
    }

    private User currentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    private boolean isAdmin(HttpSession session) {
        User user = currentUser(session);

        return user != null
                && "ADMIN".equals(user.getRole())
                && "ACTIVE".equalsIgnoreCase(user.getAccountStatus());
    }

    @GetMapping("/users")
    public String users(Model m, HttpSession s) throws Exception {
        if (!isAdmin(s)) return "redirect:/advertisements";
        m.addAttribute("users", users.all());
        return "adminUsers";
    }

    @PostMapping("/ban")
    public String ban(@RequestParam long id, HttpSession session) throws Exception {

        User actor = currentUser(session);

        if (!isAdmin(session) || actor == null) {
            return "redirect:/advertisements";
        }

        admin.ban(id, actor.getId());

        return "redirect:/admin/users";
    }

    @PostMapping("/unban")
    public String unban(@RequestParam long id, HttpSession session) throws Exception {

        if (!isAdmin(session)) {
            return "redirect:/advertisements";
        }

        admin.unban(id);

        return "redirect:/admin/users";
    }
}