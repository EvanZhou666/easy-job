package com.qianlima.easyjob.auth.interceptor;

import com.qianlima.easyjob.auth.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不需要认证的路径
/*        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            return true;
        }

        // 检查是否已认证
        if (!sessionManager.isAuthenticated()) {
            // API请求返回401
            if (isApiRequest(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            // 页面请求重定向到登录页
            response.sendRedirect("/login");
            return false;
        }*/
        return true;
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/login") || 
               path.startsWith("/static") || 
               path.startsWith("/css") || 
               path.startsWith("/js") || 
               path.startsWith("/images") ||
               path.equals("/");
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") != null && 
               request.getHeader("Accept").contains("application/json");
    }
}
