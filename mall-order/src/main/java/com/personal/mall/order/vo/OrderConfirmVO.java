package com.personal.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVO {
    List<MemberAddressVO> address;
    List<OrderItemVO> items;

    // 商品总数
    public Integer itemCount() {
        int count=0;
        if(items!=null&& !items.isEmpty()){
            for(OrderItemVO item:items){
                count+=item.getCount();
            }
        }
        return count;
    }
    // 商品总额
    public BigDecimal totalAmount() {
        BigDecimal totalAmount=new BigDecimal(0);
        if (items!=null && !items.isEmpty()){
            for(OrderItemVO item:items){
                totalAmount = totalAmount.add(item.getTotalPrice());
            }
        }
        return totalAmount;
    }
    // 需要支付的总金额
    public BigDecimal totalPayAmount() {
        return this.totalAmount();
    }
}
