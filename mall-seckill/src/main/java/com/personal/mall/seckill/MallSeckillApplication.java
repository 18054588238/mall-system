package com.personal.mall.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.personal.mall.seckill.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MallSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallSeckillApplication.class,args);
    }
}
