package com.personal.mall.thirty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    // 发送验证码
    public void sendVerificationCode(String to,String code) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("您的验证码");
            String context = "<html>" +
                    "<body>" +
                    "<h2>您好！</h2>" +
                    "<p>您的验证码是：<strong>"+code+"</strong></p>" +
                    "<p>该验证码5分钟内有效，请勿泄露给他人。</p>" +
                    "</body>" +
                    "</html>";
            helper.setText(context,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败",e);
        }
    }
}
