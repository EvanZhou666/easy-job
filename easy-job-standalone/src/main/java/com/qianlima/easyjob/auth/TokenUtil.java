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

package com.qianlima.easyjob.auth;

import com.qianlima.easyjob.auth.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {
    private static final String SECRET_KEY = "your-secret-key-change-in-production";
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时
    private static final Map<String, String> tokenStorage = new HashMap<>();
    private static final Map<String, Date> tokenExpireTime = new HashMap<>();

    public String generateToken(String username) {
        // 生成token: username + 时间戳 + 密钥的MD5
        String timestamp = String.valueOf(System.currentTimeMillis());
        String tokenStr = username + ":" + timestamp;
        String token = DigestUtils.md5Hex(tokenStr + SECRET_KEY);
        
        // 存储token
        tokenStorage.put(token, username);
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        tokenExpireTime.put(token, expireDate);
        
        return token;
    }

    public String getUsernameFromToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        
        // 检查token是否存在且未过期
        String username = tokenStorage.get(token);
        if (username != null) {
            Date expireTime = tokenExpireTime.get(token);
            if (expireTime != null && expireTime.after(new Date())) {
                return username;
            }
            // token过期，清除
            tokenStorage.remove(token);
            tokenExpireTime.remove(token);
        }
        return null;
    }

    public void validateToken(String token) {
        String username = getUsernameFromToken(token);
        if (username == null) {
            throw new AuthenticationException("无效的token或token已过期");
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void removeToken(String token) {
        tokenStorage.remove(token);
        tokenExpireTime.remove(token);
    }
}
