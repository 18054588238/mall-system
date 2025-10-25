package com.personal.mall.product.feign;

import com.personal.common.utils.R;
import com.personal.mall.product.feign.fallback.SeckillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SeckillFeignService
 * @Author liupanpan
 * @Date 2025/10/10
 * @Description
 */
@FeignClient(value = "mall-seckill",fallback = SeckillFeignServiceFallBack.class)
public interface SeckillFeignService {
    @GetMapping("/seckill/getSeckillSkuBySkuId")
    public R getSeckillSkuBySkuId(@RequestParam("skuId") Long skuId);
}
