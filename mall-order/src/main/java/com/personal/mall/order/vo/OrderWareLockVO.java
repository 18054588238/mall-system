package com.personal.mall.order.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName OrderWareLockVO
 * @Author liupanpan
 * @Date 2025/9/29
 * @Description 订单项商品库存锁定
 */
@Data
@Builder
public class OrderWareLockVO {
    private Long skuId;
    private Integer lockCount;
}
