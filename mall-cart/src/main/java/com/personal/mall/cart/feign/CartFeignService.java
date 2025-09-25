package com.personal.mall.cart.feign;

import com.personal.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName CartFeignService
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description
 */
@FeignClient("mall-product")
public interface CartFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skusaleattrvalue/attr/{skuId}")
    public List<String> getAttrs(@PathVariable("skuId") Long skuId);
}
