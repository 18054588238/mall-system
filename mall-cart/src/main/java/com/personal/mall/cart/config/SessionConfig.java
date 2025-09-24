package com.personal.mall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @ClassName MySessionConfig
 * @Author liupanpan
 * @Date 2025/9/24
 * @Description 我们需要在购物车服务中 根据当前登录用的用户信息去 Redis 中查询对应的 购物车信息。
 */
@Configuration
public class SessionConfig {
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("mall.com");
        serializer.setCookieName("mall_session");
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
