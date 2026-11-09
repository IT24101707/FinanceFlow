package com.financeflow.financeflow.config;

import com.financeflow.financeflow.entity.User;
import com.financeflow.financeflow.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Makes the logged-in user's name and photo available to every Thymeleaf
 * template as ${loggedInUserName} / ${loggedInUserPhoto}, without needing
 * each controller to add them manually.
 *
 * Thymeleaf 3.1 (used by Spring Boot 3.5) no longer allows templates to read
 * #session/#request/#response directly for security reasons, so we resolve
 * the session attribute here in Java and expose it as a plain model variable.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("loggedInUserName")
    public String loggedInUserName(HttpSession session) {
        Object name = session.getAttribute("loggedInUserName");
        return name != null ? name.toString() : "User";
    }

    @ModelAttribute("loggedInUserPhoto")
    public String loggedInUserPhoto(HttpSession session) {
        Object idObj = session.getAttribute("loggedInUserId");
        if (idObj == null) {
            return null;
        }
        User user = userService.findById((Long) idObj);
        return (user != null) ? user.getPhotoUrl() : null;
    }

}
