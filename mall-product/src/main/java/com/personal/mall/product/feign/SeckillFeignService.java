package com.personal.mall.product.feign;

import com.personal.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SeckillFeignService
 * @Author liupanpan
 * @Date 2025/10/10
 * @Description
 */
@FeignClient("mall-seckill")
public interface SeckillFeignService {
    @GetMapping("/getSeckillSkuBySkuId")
    public R getSeckillSkuBySkuId(@RequestParam("skuId") Long skuId);
}
