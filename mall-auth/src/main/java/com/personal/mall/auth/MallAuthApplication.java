package com.personal.mall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName MallAuthApplication
 * @Author liupanpan
 * @Date 2025/8/27
 * @Description
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAuthApplication.class,args);
    }
}
