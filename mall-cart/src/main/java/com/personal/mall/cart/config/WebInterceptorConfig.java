package com.personal.mall.cart.config;

import com.personal.mall.cart.intercept.AuthIntercept;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebInterceptorConfig
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description 注册拦截器
 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthIntercept()).addPathPatterns("/**");//拦截所有路径
    }
}
