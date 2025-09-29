package com.personal.mall.order.web;

import com.personal.common.exception.WareNoStockException;
import com.personal.mall.order.service.OrderService;
import com.personal.mall.order.vo.OrderConfirmVO;
import com.personal.mall.order.vo.OrderResponseVO;
import com.personal.mall.order.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String orderSubmit(OrderSubmitVO vo, Model model, RedirectAttributes redirectAttributes) {
        OrderResponseVO orderResponseVO = null;
        Integer code = 0;
        try {
            orderResponseVO = orderService.orderSubmit(vo);
            System.out.println(orderResponseVO);
            code = orderResponseVO.getCode();
        } catch (WareNoStockException e) {
            code = 2;
        }


        if (code == 0 ){
            // 创建订单成功
            model.addAttribute("orderResponseVO",orderResponseVO);
            return "pay";
        } else{
            String msg = "创建订单失败-";
            if (code == 1) {
                msg += "重复提交";
            }else if (code == 2) {
                msg += "库存不足";
            }
            // 从一个 URL 重定向到另一个 URL
            // 重定向后，Flash 属性会自动添加到服务于目标 URL 的控制器的模型中。
            redirectAttributes.addFlashAttribute("msg",msg);//携带参数重定向【请求】
            return "redirect:http://order.mall.com/toTrade";
        }
    }
}
