package com.personal.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
public class OrderPayListenerController {
    // 支付成功的回调接口(异步)
    @PostMapping("/payed/notifyUrl")
    public String aliPayedHandle(HttpServletRequest request) {
        System.out.println("支付成功的回调接口(异步)");
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.keySet().forEach(key -> {
            System.out.println("------->key:"+key+":"+ Arrays.toString(parameterMap.get(key)));
        });
        return "success";
    }
}
