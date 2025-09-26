package com.personal.mall.order.interceptor;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.AuthConstant;
import com.personal.common.vo.MemberVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName AuthInterceptor
 * @Author liupanpan
 * @Date 2025/9/26
 * @Description 订单服务必须 要认证信息
 */
public class AuthInterceptor implements HandlerInterceptor {
    // 创建线程
    public static ThreadLocal<MemberVO> threadLocal = new ThreadLocal<>();
    // 拦截所有请求

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if (attribute != null) {
            MemberVO memberVO = (MemberVO) attribute;
            threadLocal.set(memberVO);
            return true;
        }
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.mall.com/login");
        return false;
    }
}
