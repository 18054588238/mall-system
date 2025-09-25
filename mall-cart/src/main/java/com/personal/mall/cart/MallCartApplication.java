package com.personal.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName MallCartApplication
 * @Author liupanpan
 * @Date 2025/9/24
 * @Description
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCartApplication.class,args);
    }
}
