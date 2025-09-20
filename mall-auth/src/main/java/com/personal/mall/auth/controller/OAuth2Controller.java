package com.personal.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.personal.common.utils.HttpUtils;
import com.personal.mall.auth.vo.AuthResponseVO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OAuth2Controller
 * @Author liupanpan
 * @Date 2025/9/20
 * @Description
 */
@Controller
public class OAuth2Controller {

    @RequestMapping("/oauth2/weibo/authorize")
    public String weiboAuth(@RequestParam("code") String code) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("client_id","");
        map.put("client_secret","");
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("redirect_uri","http://mall.auth.com/oauth2/weibo/authorize");
        // 获取code，进一步获取token
        HttpResponse post = HttpUtils.doPost("https://api.weibo.com",
                "/oauth2/access_token",
                "post",
                new HashMap<>(),
                new HashMap<>(),
                map);
        int statusCode = post.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            // 登录失败
            return "redirect:http://mall.auth.com/login";
        }
        // getClass().getName() + '@' + Integer.toHexString(hashCode())
        HttpEntity entity = post.getEntity();
        String string = EntityUtils.toString(entity); // 返回HttpEntity实体的字符串
        AuthResponseVO authResponseVO = JSON.parseObject(string, AuthResponseVO.class);
        // 获取到token，进行登录操作

        return "";
    }
}
