package com.personal.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 开启远程调用 basePackages 指定Fegin接口的路径
 */
@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.personal.mall.order.feign")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.personal.mall.order.dao")
public class MallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallOrderApplication.class, args);
	}

}
