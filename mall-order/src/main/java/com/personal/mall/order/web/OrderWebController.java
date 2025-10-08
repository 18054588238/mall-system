package com.personal.mall.order.web;

import com.alipay.api.AlipayApiException;
import com.personal.common.exception.WareNoStockException;
import com.personal.mall.order.config.MyAlipayConfig;
import com.personal.mall.order.service.OrderService;
import com.personal.mall.order.vo.OrderConfirmVO;
import com.personal.mall.order.vo.OrderResponseVO;
import com.personal.mall.order.vo.OrderSubmitVO;
import com.personal.mall.order.vo.PayVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderWebController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MyAlipayConfig alipayConfig;

    // 点击支付宝 获取订单信息，跳转到支付页面
    @GetMapping(value = "/aliOrderPay", produces = "text/html")
    @ResponseBody
    public String aliOrderPay(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        // 获取订单编号，订单金额等信息进行验签
        PayVO payVO = orderService.getOrderPay(orderSn);
        // 通过表单生成的地址
        String pay = alipayConfig.pay(payVO);
        /*<form name="punchout_form" method="post"
        action="https://openapi.alipay.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=kpVC9JN76NzUK3EPKkA3YbAlIb8pIKrT%2FPD1PBk8TThFLis6pbcPYuG%2FnxnM7eJSfm4tdv8KyW5OeibMd6IlQJFnKJ0YcPrI%2BZjgJY%2BC6%2BiM4AvzuI%2BwhInRW%2B7zJwCNyqVZRWmTnKEm4kcNjiQ8R%2BQTWBVwFFATDY%2B4I1%2BvJi%2BHLMp2vVfFANOvscgoR5J1KedIttc%2Fp7GttDc7M6SO7Fmc5sibGxPBFbpadtQDozuTRPY694JyF57P2hqDy3grMuy%2BK0oz1Fj3l6PrLVVNB7INfA%2FsAIjdPIuPeC%2BGZtb%2FxRulC3NM4w8STCF3yQsVU7V3pzw7P8iIeBbs4BBXJQ%3D%3D&return_url=http%3A%2F%2Forder.mall.com%2ForderPay%2FreturnUrl
        &notify_url=http%3A%2F%2Forder.mall.com%2Fpayed%2FnotifyUrl
        &version=1.0
        &app_id=9021000156614832
        &sign_type=RSA2
        &timestamp=2025-10-07+11%3A52%3A55
        &alipay_sdk=alipay-sdk-java-dynamicVersionNo&format=json">
<input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;202510071151543441975408717985751041&quot;,&quot;total_amount&quot;:&quot;6999.00&quot;,&quot;subject&quot;:&quot;123456q&quot;,&quot;body&quot;:&quot;null&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
<input type="submit" value="立即支付" style="display:none" >
</form>*/
        return pay;
    }

    // 同步回调接口
    @GetMapping("/orderPay/returnUrl")
    public String returnUrl(@RequestParam(value = "orderSn",required = false) String orderSn,@RequestParam(value = "out_trade_no",required = false) String outTradeNo) {
        // TODO 完成订单支付成功后相关的一系列操作
        System.out.println(outTradeNo+"------" +orderSn);
        if (StringUtils.isNotEmpty(outTradeNo)) {
            orderService.orderPayedHandle(outTradeNo);
        } else {
            orderService.orderPayedHandle(orderSn);
        }
        return "list";
    }

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
