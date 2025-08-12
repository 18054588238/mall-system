package com.personal.mall.thirty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient  // 放开注册中心
@SpringBootApplication
public class MallThirtyPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallThirtyPartyApplication.class, args);
    }
}
