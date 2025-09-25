package com.personal.mall.cart.intercept;

import com.personal.common.constant.AuthConstant;
import com.personal.common.vo.MemberVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName AuthIntercept
 * @Author liupanpan
 * @Date 2025/9/24
 * @Description 自定义拦截器：帮助我们获取当前登录的用户信息
 *  *     通过Session共享获取的
 */
public class AuthIntercept implements HandlerInterceptor {
    // 本地线程对象
    public static ThreadLocal<MemberVO> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if (attribute != null) {
            MemberVO memberVO = (MemberVO) attribute;
            threadLocal.set(memberVO);
            return true;
        }
        // attribute == null,说明没有登录
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.mall.com/login");
        return false;
    }
}
