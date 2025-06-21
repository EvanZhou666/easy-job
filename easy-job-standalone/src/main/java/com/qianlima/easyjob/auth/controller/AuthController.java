package com.qianlima.easyjob.auth.controller;

import com.qianlima.easyjob.auth.repository.UserRepository;
import com.qianlima.easyjob.auth.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        
//        userRepository.findByUsername(username).ifPresentOrElse(user -> {
//            if (user.getStatus() == 0) {
//                result.put("success", false);
//                result.put("message", "账号已被禁用");
//            } else if (PasswordUtils.verifyPassword(password, user.getPassword())) {
//                sessionManager.setCurrentUser(username);
//                result.put("success", true);
//                result.put("message", "登录成功");
//            } else {
//                result.put("success", false);
//                result.put("message", "用户名或密码错误");
//            }
//        }, () -> {
//            result.put("success", false);
//            result.put("message", "用户名或密码错误");
//        });
        result.put("success", true);
        result.put("message", "欢迎");
        return result;
    }

    @GetMapping("/logout")
    public String logout() {
        sessionManager.logout();
        return "redirect:/login";
    }
}
