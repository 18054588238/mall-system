package com.personal.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @ClassName MallCartApplication
 * @Author liupanpan
 * @Date 2025/9/24
 * @Description
 */
@EnableFeignClients
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class MallCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCartApplication.class,args);
    }
}
