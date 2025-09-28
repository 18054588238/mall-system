package com.personal.mall.order.web;

import com.personal.mall.order.service.OrderService;
import com.personal.mall.order.vo.OrderConfirmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String orderSubmit() {
        return "pay";
    }
}
