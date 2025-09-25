package com.personal.mall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @ClassName SessionConfig
 * @Author liupanpan
 * @Date 2025/9/22
 * @Description 自定义cookie实现session域名的调整
 * 自定义Cookie主要通过调整Cookie的Domain属性来实现session域名的调整，
 * 使其跨域或限定在特定子域下，从而实现不同域名之间的session共享或隔离。
 * 默认行为：:默认情况下，Cookie的Domain属性限制其只能被相同域名下的网站访问。
 *
 * 跨域实现：:通过设置Domain属性为一个父域名（例如，如果你的主域名是example.com，你可以将Domain设置为`.example.com`），
 * 那么同一父域名下的所有子域名（如sub1.example.com、sub2.example.com）都可以访问该Cookie，从而共享session信息。
 *
 * 实际应用场景
 * 单点登录(SSO)：在一个父域名下部署多个子域的应用时，可以通过设置父域的Cookie共享用户的登录状态，实现跨子域的SSO。
 */
@Configuration
public class SessionConfig {
    /*自定义cookie配置*/
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setDomainName("mall.com"); // 一级域名
        cookieSerializer.setCookieName("mall_session");
        return cookieSerializer;
    }

    /*对存储在redis中的数据指定序列化的方式*/
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
