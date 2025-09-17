package com.personal.mall.auth.controller;

import com.personal.common.constant.MailConstant;
import com.personal.common.exception.BizCodeEnum;
import com.personal.common.utils.R;
import com.personal.mall.auth.feign.ThirtyPartyFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
public class AuthController {

    @Autowired
    private ThirtyPartyFeignService thirtyPartyFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/send/mail")
    public R sendCode(@RequestParam("to") String to) {
        String prefix = MailConstant.MAIL_CODE_PREFIX;
        // 从缓存中获取验证码
        String s = redisTemplate.opsForValue().get(prefix + to);
        if (s != null && !s.isEmpty()) {
            // 当验证码发送时间不足1min，而又点击发送验证码时，返回给前端提示信息
            String time = s.split("_")[1];
            if (System.currentTimeMillis() - Long.parseLong(time) <= 60000) {
                return R.error(BizCodeEnum.VALID_MAIL_EXCEPTION.getCode(),BizCodeEnum.VALID_MAIL_EXCEPTION.getMsg());
            }
        }
        // 邮箱由前端传递
        // 随机生成6位验证码
        String code = generateCode();
        thirtyPartyFeignService.sendMail(to,code);
        // 将验证码存到缓存中
        redisTemplate.opsForValue().set(prefix + to ,code+"_"+System.currentTimeMillis(),5, TimeUnit.MINUTES);
        return R.ok();
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
}
