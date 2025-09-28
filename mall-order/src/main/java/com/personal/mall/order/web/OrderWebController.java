package com.personal.mall.order.web;

import com.personal.mall.order.service.OrderService;
import com.personal.mall.order.vo.OrderConfirmVO;
import com.personal.mall.order.vo.OrderResponseVO;
import com.personal.mall.order.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) {
        OrderConfirmVO confirmVO = orderService.confirmOrder();
        model.addAttribute("confirmVO",confirmVO);
        return "confirm";
    }

    @PostMapping("/orderSubmit")
    public String orderSubmit(OrderSubmitVO vo,Model model) {
        // 响应
        OrderResponseVO orderResponseVO = orderService.orderSubmit(vo);
        if (orderResponseVO.getCode() == 0) {
            // 订单生成成功
        }else {
            // 订单生成失败
        }
        return "pay";
    }
}
