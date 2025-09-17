package com.personal.mall.thirty.controller;

import com.personal.common.utils.R;
import com.personal.mall.thirty.service.EmailService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/mail/send")
    public R sendMail(@RequestParam("to") String to, @RequestParam("code") String code){
        emailService.sendVerificationCode(to,code);
        return R.ok();
    }
}
