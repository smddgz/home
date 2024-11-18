package org.example.service;


import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class MailService {
    public void sendMail(String text) throws MessagingException {
        String host = "smtp.163.com";
// 登录用户名:
        String username = "moresleep2020@163.com";
// 登录口令:
        String password = "YYeJcWWYzzuTXUWt";
// 连接到SMTP服务器587端口:
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");
// 获取Session实例:
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
// 设置debug模式便于调试:
        System.out.println(session.getTransport());
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
// 设置发送方地址:
        message.setFrom(new InternetAddress(username));
// 设置接收方地址:
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("736956467@qq.com"));
// 设置邮件主题:
        message.setSubject("Hello", "UTF-8");
// 设置邮件正文:
        message.setText(text, "UTF-8");
// 发送:
        Transport.send(message);
    }
}
