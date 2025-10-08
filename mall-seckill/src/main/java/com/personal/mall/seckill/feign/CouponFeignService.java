package com.personal.mall.seckill.feign;

import com.personal.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/seckillsession/getSeckillSkuLatestDays")
    R getSeckillSkuLatestDays(@RequestBody String[] latestDays);
}
