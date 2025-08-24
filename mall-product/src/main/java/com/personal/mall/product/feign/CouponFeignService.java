package com.personal.mall.product.feign;

import com.personal.common.dto.SkuReductionDTO;
import com.personal.common.dto.SpuBoundsDTO;
import com.personal.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveFullReductionInfo(@RequestBody SkuReductionDTO dto);

    @PostMapping("/coupon/spubounds/saveinfo")
    R saveSpuBounds(@RequestBody SpuBoundsDTO dto);
}
