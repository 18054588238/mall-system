package com.personal.mall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.personal.common.constant.AuthConstant;
import com.personal.mall.cart.intercept.AuthIntercept;
import com.personal.mall.cart.service.CartService;
import com.personal.mall.cart.vo.CartItemVO;
import com.personal.mall.cart.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName CartController
 * @Author liupanpan
 * @Date 2025/9/25
 * @Description
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    // 获取当前用户下被选中的购物车列表
    @RequestMapping(value = "/checkCartList")
    @ResponseBody
    public List<CartItemVO> checkCartList() {
        return cartService.checkCartList();
    }

    // 添加商品到购物车
    @GetMapping("/addCart")
    public String addCart(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num, Model model) {
        try {
            CartItemVO cartItemVO = cartService.addCart(skuId,num);
            model.addAttribute("item", cartItemVO);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    // 获取购物车列表
    @GetMapping("/cartList")
    public String getCartList(Model model) {
        CartVO vo = cartService.getCartList();
        model.addAttribute("cart",vo);
        return "cartList";
    }
}
