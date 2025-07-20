/*
 * MIT License
 *
 * Copyright (c) 2025 EvanZhou666
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package site.qianlima.easyjob.auth.interceptor;

import site.qianlima.easyjob.auth.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.qianlima.easyjob.config.EasyJobProps;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;
    private final EasyJobProps easyJobProps;



    public AuthInterceptor(SessionManager sessionManager, EasyJobProps easyJobProps) {
        this.sessionManager = sessionManager;
        this.easyJobProps = easyJobProps;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (isPublicPath(request.getContextPath(), path) || isWhitePath(request.getContextPath(), path)) {
            return true;
        }

        if (!sessionManager.isAuthenticated()) {
            if (isApiRequest(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private boolean isPublicPath(String contextPath, String path) {
        return path.startsWith(contextPath + "/login") ||
               path.startsWith(contextPath + "/static") ||
               path.startsWith(contextPath + "/css") ||
               path.startsWith(contextPath + "/static/js") ||
               path.startsWith(contextPath + "/images");
    }

    private boolean isWhitePath(String contextPath, String path) {
        if (easyJobProps.getWhiteList() != null && !easyJobProps.getWhiteList().isEmpty()) {
            return easyJobProps.getWhiteList().contains(path.replaceFirst(contextPath, ""));
        }
        return false;
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") != null && 
               request.getHeader("Accept").contains("application/json");
    }
}
