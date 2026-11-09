package com.financeflow.financeflow.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// Blocks access to protected pages unless the user is logged in
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("loggedInUserId") != null);

        if (!loggedIn) {
            response.sendRedirect("/api/users/login-page");
            return false;
        }

        return true;
    }

}
