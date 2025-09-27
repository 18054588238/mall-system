package com.personal.mall.order.feign;

import com.personal.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/*@FeignClient 指明我们要从注册中心中发现的服务的名称*/
@FeignClient(value = "mall-product")
public interface ProductServiceFeign {
    // 需要访问的远程方法
    @GetMapping("/product/brand/all")
    public R queryAllBrand();

}
