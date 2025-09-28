package com.personal.mall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MallFeginConfig
 * @Author liupanpan
 * @Date 2025/9/28
 * @Description 解决feign调用时，Header请求头丢失问题
 * cookie中保存了认证信息，而购物车模块需要登录状态下才可以进入，cookie丢失后就无法进入了
 * 解决方案
 * 首先需要写一个 Feign请求拦截器，通过实现RequestInterceptor接口,完成对所有的Feign请求,传递请求头和请求参数。（下面没有使用这种方法）
 */
@Configuration
public class MallFeignConfig {
    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                System.out.println("RequestInterceptor"+Thread.currentThread().getName());
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    String cookie = attributes.getRequest().getHeader("Cookie");
                    template.header("Cookie",cookie);
                }
            }
        };
    }
}
