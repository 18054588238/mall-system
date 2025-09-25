package com.personal.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.CartConstant;
import com.personal.common.utils.R;
import com.personal.common.vo.MemberVO;
import com.personal.mall.cart.feign.CartFeignService;
import com.personal.mall.cart.intercept.AuthIntercept;
import com.personal.mall.cart.service.CartService;
import com.personal.mall.cart.vo.CartItemVO;
import com.personal.mall.cart.vo.CartVO;
import com.personal.mall.cart.vo.SkuInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName CartServiceIMpl
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public CartItemVO addCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        // 获取当前用户下的购物车信息
        BoundHashOperations<String, Object, Object> boundHashOperations = getBoundHashOperations();
        // 先查redis，如果redis已经有了，则更新数量即可，如果redis没有，则新增购物车商品
        Object o = boundHashOperations.get(skuId.toString());
        if (o != null) {
            String cartInfos = (String) o;
            CartItemVO cartItemVO = JSON.parseObject(cartInfos, CartItemVO.class);
            cartItemVO.setCount(cartItemVO.getCount()+num);

            boundHashOperations.put(skuId.toString(),JSON.toJSONString(cartItemVO));
            return cartItemVO;
        }
        AtomicReference<SkuInfoVO> skuInfo = new AtomicReference<>();
        // 配置线程池，异步处理
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            // 商品基本信息
            R info = cartFeignService.info(skuId);
            String skuInfoJson = (String) info.get("skuInfoJson");
            skuInfo.set(JSON.parseObject(skuInfoJson,SkuInfoVO.class));
        }, threadPoolExecutor);

        AtomicReference<List<String>> attrs = new AtomicReference<>();
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            // 属性属性列表
            attrs.set(cartFeignService.getAttrs(skuId));
        }, threadPoolExecutor);

        CompletableFuture.allOf(future1,future2).get();

        CartItemVO cartItemVO = CartItemVO.builder()
                .check(true)
                .image(skuInfo.get().getSkuDefaultImg())
                .spuId(skuInfo.get().getSpuId())
                .title(skuInfo.get().getSkuTitle())
                .price(skuInfo.get().getPrice())
                .count(num)
                .skuAttr(attrs.get())
                .skuId(skuId)
                .build();

        // 存到redis中
        boundHashOperations.put(skuId.toString(), JSON.toJSONString(cartItemVO));

        return cartItemVO;
    }

    @Override
    public CartVO getCartList() {
        BoundHashOperations<String, Object, Object> hashOperations = getBoundHashOperations();
        // 获取该用户下的所有购物车信息
        CartVO cart = new CartVO();
        List<CartItemVO> itemVOS = new ArrayList<>();
        List<Object> values = hashOperations.values();
        for (Object value : values) {
            String json = (String) value;
            CartItemVO cartItemVO = JSON.parseObject(json, CartItemVO.class);
            itemVOS.add(cartItemVO);
        }
        cart.setItems(itemVOS);
        return cart;
    }

    private BoundHashOperations<String, Object, Object> getBoundHashOperations() {
        // prefix+userId
        // 获取当前登录用户信息
        MemberVO memberVO = AuthIntercept.threadLocal.get();
        return redisTemplate.boundHashOps(CartConstant.CART_REDIS_PREFIX + memberVO.getId());
    }
}
