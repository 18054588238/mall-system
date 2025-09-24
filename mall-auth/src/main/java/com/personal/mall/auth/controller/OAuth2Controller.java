package com.personal.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.AuthConstant;
import com.personal.common.utils.HttpUtils;
import com.personal.common.utils.R;
import com.personal.common.vo.MemberVO;
import com.personal.mall.auth.feign.MemberServiceFeign;
import com.personal.mall.auth.vo.AuthResponseVO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
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

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    /*
    * 正确的请求会在浏览器内打开一个授权确认页面，
    * 当用户同意授权后，会重定向到授权回调地址，
    * 并带上回传的数据
    * http://www.example.com/response?code=CODE
    * */
    @RequestMapping("/oauth2/weibo/authorize")
    public String weiboAuth(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("client_id","3347564093");
        map.put("client_secret","56ff673d468499b15618d778ae40be70");
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("redirect_uri","http://auth.mall.com/oauth2/weibo/authorize");
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
            return "redirect:http://auth.mall.com/login";
        }
        // getClass().getName() + '@' + Integer.toHexString(hashCode())
        HttpEntity entity = post.getEntity();
        String string = EntityUtils.toString(entity); // 返回HttpEntity实体的字符串
        AuthResponseVO authResponseVO = JSON.parseObject(string, AuthResponseVO.class);
        // 获取到token，进行登录操作
        R r = memberServiceFeign.oauthLogin(authResponseVO);
        if (r.getCode() != 0) {
            // 登录错误
            return "redirect:http://auth.mall.com/login";
        }
        // 赋值 R.ok().put("entity", JSON.toJSONString(entity))
        String entity1 = (String) r.get("entity");
        MemberVO memberVO = JSON.parseObject(entity1, MemberVO.class);
        session.setAttribute(AuthConstant.AUTH_SESSION_REDIS,memberVO);
        return "redirect:http://system.mall.com/index";
    }
}
