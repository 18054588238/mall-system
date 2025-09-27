package com.personal.mall.cart.service;

import com.personal.mall.cart.vo.CartItemVO;
import com.personal.mall.cart.vo.CartVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName CartService
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description
 */
public interface CartService {
    CartItemVO addCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartVO getCartList();

    List<CartItemVO> checkCartList();
}
