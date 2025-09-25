package com.personal.mall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @ClassName MallSearchApplication
 * @Author liupanpan
 * @Date 2025/8/27
 * @Description
 */
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class MallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallSearchApplication.class,args);
    }
}
