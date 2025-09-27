package com.personal.mall.order.feign;

import com.personal.mall.order.vo.OrderItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(value = "mall-cart")
public interface CartFeignService {
    @RequestMapping(value = "/checkCartList",produces = "application/json")
    @ResponseBody
    public List<OrderItemVO> checkCartList();
}
