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
