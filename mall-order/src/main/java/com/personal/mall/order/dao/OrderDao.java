package com.personal.mall.order.dao;

import com.personal.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:07:15
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderStatus(@Param("orderSn") String orderSn, @Param("status") int status);
}
