package com.qianlima.easyjob.auth.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionManager {
    private static final String USER_KEY = "CURRENT_USER";
    private static final int SESSION_TIMEOUT = 30 * 60; // 30分钟

    public void setCurrentUser(String username) {
        HttpSession session = getSession();
        session.setAttribute(USER_KEY, username);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
    }

    public String getCurrentUser() {
        HttpSession session = getSession();
        return (String) session.getAttribute(USER_KEY);
    }

    public void logout() {
        HttpSession session = getSession();
        session.removeAttribute(USER_KEY);
        session.invalidate();
    }

    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
