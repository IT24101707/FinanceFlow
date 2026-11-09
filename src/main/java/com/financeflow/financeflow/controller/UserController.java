package com.financeflow.financeflow.controller;

import com.financeflow.financeflow.entity.User;
import com.financeflow.financeflow.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registration Page
    @GetMapping("/")
    public String registerPage() {
        return "register";
    }

    // Login Page
    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    // Register User
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "This email is already registered!");
            return "register";
        }

        user.setCreatedDate(LocalDate.now());
        userService.registerUser(user);

        return "redirect:/api/users/login-page?registered=true";
    }

    // Login User
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        User user = userService.authenticate(email, password);

        if (user != null) {
            session.setAttribute("loggedInUserId", user.getId());
            session.setAttribute("loggedInUserName", user.getFullName());
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid email or password!");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/api/users/login-page";
    }

}
