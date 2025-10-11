package com.personal.mall.seckill.config;

import com.personal.mall.seckill.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebInterceptorConfig
 * @Author liupanpan
 * @Date 2025/10/11
 * @Description
 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/seckill/onSeckill");
    }
}
