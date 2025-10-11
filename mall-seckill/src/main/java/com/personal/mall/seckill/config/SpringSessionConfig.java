package com.personal.mall.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @ClassName SpringSessionConfig
 * @Author liupanpan
 * @Date 2025/10/11
 * @Description
 */
@Configuration
public class SpringSessionConfig {
    // 1.自定义cookie配置
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("mall.com");// 允许跨域和应用程序共享会话
        cookieSerializer.setCookieName("mall_session");
        return cookieSerializer;
    }
    // 2.redis数据序列化
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
