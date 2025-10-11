package com.personal.mall.seckill.interceptor;

import com.personal.common.constant.AuthConstant;
import com.personal.common.vo.MemberVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName AuthInterceptor
 * @Author liupanpan
 * @Date 2025/10/11
 * @Description 对于必须要进行登录认证的服务，添加此类
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static ThreadLocal<MemberVO> threadLocal = new ThreadLocal<>() ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(AuthConstant.AUTH_SESSION_REDIS);
        if (attribute != null) {
            MemberVO vo = (MemberVO) attribute;
            threadLocal.set(vo);
            return true;
        }
        session.setAttribute(AuthConstant.AUTH_SESSION_MSG,"请先登录");
        response.sendRedirect("http://auth.mall.com/login");
        return false;
    }
}
