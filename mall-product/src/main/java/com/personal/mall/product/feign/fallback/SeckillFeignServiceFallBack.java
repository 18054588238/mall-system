package com.personal.mall.product.feign.fallback;

import com.personal.common.exception.BizCodeEnum;
import com.personal.common.utils.R;
import com.personal.mall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SeckillFeignServiceFallBack
 * @Author liupanpan
 * @Date 2025/10/24
 * @Description
 */
@Slf4j
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSeckillSkuBySkuId(@RequestParam("skuId") Long skuId) {
        log.error("熔断降级....SeckillFeignService:{}",skuId);
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(),BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }
}
