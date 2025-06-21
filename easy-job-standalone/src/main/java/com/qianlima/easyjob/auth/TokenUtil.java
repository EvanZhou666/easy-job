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
