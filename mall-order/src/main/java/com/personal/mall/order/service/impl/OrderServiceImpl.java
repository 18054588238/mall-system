package com.personal.mall.order.service.impl;

import com.personal.common.constant.OrderConstant;
import com.personal.common.vo.MemberVO;
import com.personal.mall.order.feign.CartFeignService;
import com.personal.mall.order.feign.MemberServiceFeign;
import com.personal.mall.order.interceptor.AuthInterceptor;
import com.personal.mall.order.vo.*;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.common.utils.PageUtils;
import com.personal.common.utils.Query;

import com.personal.mall.order.dao.OrderDao;
import com.personal.mall.order.entity.OrderEntity;
import com.personal.mall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private MemberServiceFeign memberServiceFeign;
    @Autowired
    private CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate redisTemplate;

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

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 异步编排
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(attributes);
            // 获取当前登录用户的地址信息
            List<MemberAddressVO> addressInfo = memberServiceFeign.getAddressInfo(memberVOId);
            confirmVO.setAddress(addressInfo);
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            // 保存认证信息
            RequestContextHolder.setRequestAttributes(attributes);
            // 获取当前登录用户的购物车信息
            List<OrderItemVO> orderItemVOS = cartFeignService.checkCartList();
            confirmVO.setItems(orderItemVOS);
        });

        try {
            CompletableFuture.allOf(future1,future2).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        /*①客户端请求获取token，服务端生成一个唯一ID作为token存在redis中；
          ②客户端第二次请求时携带token，服务端校验token成功则执行业务操作并删除token，服务端校验token失败则表示重复操作。
        */
        // 生成防重的token，保存到redis，并绑定给前端
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 存储到redis中, order:token:用户编号
        redisTemplate.opsForValue().set(OrderConstant.ORDER_TOKEN_PREFIX+":"+memberVOId,token);
        confirmVO.setOrderToken(token);// 响应给前端
        return confirmVO;
    }

    @Override
    public OrderResponseVO orderSubmit(OrderSubmitVO vo) {
        OrderResponseVO responseVO = new OrderResponseVO();
        String orderToken = vo.getOrderToken();
        // 获取当前用户信息
        MemberVO memberVO = AuthInterceptor.threadLocal.get();
        String key = OrderConstant.ORDER_TOKEN_PREFIX + ":" + memberVO.getId();
        // 防重
        String token = redisTemplate.opsForValue().get(key);
        // 通过脚本实现原子操作
        String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0";
        Long l = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(token), orderToken);

        if (l == 0) {
            // 表示重复提交
            responseVO.setCode(1);
            return responseVO;
        }
        // 生成订单


        return responseVO;
    }

}