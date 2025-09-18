package com.personal.mall.auth.controller;

import com.personal.common.constant.MailConstant;
import com.personal.common.exception.BizCodeEnum;
import com.personal.common.utils.R;
import com.personal.mall.auth.feign.MemberServiceFeign;
import com.personal.mall.auth.feign.ThirtyPartyFeignService;
import com.personal.mall.auth.vo.RegisterUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
public class AuthController {

    @Autowired
    private ThirtyPartyFeignService thirtyPartyFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MemberServiceFeign memberServiceFeign;

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

    @PostMapping("/auth/register")
    public String register(@Valid RegisterUserVO vo, BindingResult result, Model model) {
        Map<String, String> map = new HashMap<>();
        // 校验注册页面输入数据是否合法
        boolean b = registerValid(result,model,vo,map);
        // 校验不通过，返回注册页面
        if (!b) {
            return "/register";
        }
        // 校验通过，进行注册服务
        R register = memberServiceFeign.register(vo);
        if (register.getCode() != 0) {
            // 注册失败。返回错误信息
            map.put("msg",register.getMessage(register.getCode()));
            model.addAttribute("error",map);
            return "/register";
        }

        return "redirect:http://mall.auth.com/login";
    }

    // 与前端交互，校验注册页面输入数据是否合法，BindingResult 可以获取哪些字段校验错误及错误信息，通过Model将校验错误的返回给前端
    private boolean registerValid(BindingResult result, Model model,RegisterUserVO vo,Map<String, String> map ) {

        if (result.hasFieldErrors()) {

            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            model.addAttribute("error",map);
            return false;
        }
        // 校验验证码
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(MailConstant.MAIL_CODE_PREFIX + vo.getEmail());
        if (StringUtils.isNotBlank(s) && s.split("_")[0].equalsIgnoreCase(code)) {
            // 验证码正确，删除缓存
            redisTemplate.delete(MailConstant.MAIL_CODE_PREFIX + vo.getEmail());
            return true;
        } else {
            // 验证码不正确，返回错误信息
            map.put("code","验证码错误");
            model.addAttribute("error",map);
            return false;
        }
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
}
