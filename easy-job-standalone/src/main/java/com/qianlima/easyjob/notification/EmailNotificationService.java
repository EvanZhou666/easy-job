package com.qianlima.easyjob.notification;

import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailNotificationService implements NotificationService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${notification.email.from:}")
    private String fromEmail;

    @Value("${notification.email.to:}")
    private String toEmail;

    @Override
    public void sendNotification(String title, String content) {
        if (mailSender != null && fromEmail != null && toEmail != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(title);
            message.setText(content);
//            mailSender.send(message);
        }
    }
}
