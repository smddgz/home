package org.example.controller;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@Slf4j
@RestController
public class MailController {
    @Autowired
    MailService mailService;

    @GetMapping("sendMail")
    public String sendMail(){
        try {
            mailService.sendMail();
        } catch (MessagingException e) {
            log.error(e.getMessage());
            return "send error";
        }
        return "send success";
    }
}
