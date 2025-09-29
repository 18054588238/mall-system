package com.personal.mall.order.feign;

import com.personal.common.utils.R;
import com.personal.mall.order.vo.OrderWareLockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName WareFeignService
 * @Author liupanpan
 * @Date 2025/9/29
 * @Description
 */
@FeignClient("mall-ware")
public interface WareFeignService {
    @RequestMapping("/ware/waresku/lockWareStock")
    public R lockWareStock(@RequestBody List<OrderWareLockVO> vos);
}
