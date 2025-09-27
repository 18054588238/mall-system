package com.personal.mall.order.service.impl;

import com.personal.common.vo.MemberVO;
import com.personal.mall.order.feign.CartFeignService;
import com.personal.mall.order.feign.MemberServiceFeign;
import com.personal.mall.order.interceptor.AuthInterceptor;
import com.personal.mall.order.vo.MemberAddressVO;
import com.personal.mall.order.vo.OrderConfirmVO;
import com.personal.mall.order.vo.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.order.dao.OrderDao;
import com.personal.mall.order.entity.OrderEntity;
import com.personal.mall.order.service.OrderService;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private MemberServiceFeign memberServiceFeign;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO confirmOrder() {
        OrderConfirmVO confirmVO = new OrderConfirmVO();
        MemberVO memberVO = AuthInterceptor.threadLocal.get();
        Long memberVOId = memberVO.getId();
        // 异步执行
//        CompletableFuture.runAsync(() -> {
//
//        })
        // 获取当前登录用户的地址信息
        List<MemberAddressVO> addressInfo = memberServiceFeign.getAddressInfo(memberVOId);
        confirmVO.setAddress(addressInfo);
        // 获取当前登录用户的购物车信息
        List<OrderItemVO> orderItemVOS = cartFeignService.checkCartList();
        confirmVO.setItems(orderItemVOS);
        return confirmVO;
    }

}