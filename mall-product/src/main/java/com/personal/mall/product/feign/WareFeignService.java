package com.personal.mall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName WareFeignService
 * @Author liupanpan
 * @Date 2025/8/28
 * @Description
 */
@FeignClient("mall-ware")
public interface WareFeignService {
    @GetMapping("/ware/waresku/haveStock")
    List<Long> haveStock(List<Long> skuIds);
}
