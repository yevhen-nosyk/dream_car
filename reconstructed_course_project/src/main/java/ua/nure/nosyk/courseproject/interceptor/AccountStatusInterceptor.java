package ua.nure.nosyk.courseproject.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import ua.nure.nosyk.courseproject.entity.User;
import ua.nure.nosyk.courseproject.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AccountStatusInterceptor implements HandlerInterceptor {

    private final UserService users;

    public AccountStatusInterceptor(UserService users) {
        this.users = users;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // A guest has no logged-in account to check.
        if (session == null) {
            return true;
        }

        Object value = session.getAttribute("currentUser");

        if (!(value instanceof User)) {
            return true;
        }

        User sessionUser = (User) value;

        if (sessionUser.getId() == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Always fetch the latest account from MySQL.
        User currentUser = users.find(sessionUser.getId());

        if (currentUser == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Passwords are not needed in the session.
        currentUser.setPassword(null);
        session.setAttribute("currentUser", currentUser);

        boolean banned =
                "BANNED".equalsIgnoreCase(currentUser.getAccountStatus());

        if (!banned) {
            return true;
        }

        String path = request.getServletPath();

        // A banned user may see the blocked-account page or log out.
        if ("/banned".equals(path) || "/logout".equals(path)) {
            return true;
        }

        // Block all other controller requests, regardless of HTTP method
        // or whether the user followed a navigation link or typed a URL.
        response.sendRedirect(request.getContextPath() + "/banned");
        return false;
    }
}