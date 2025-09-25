package com.personal.mall.cart.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CartItemVO
 * @Author liupanpan
 * @Date 2025/9/24
 * @Description 购物车中商品的信息
 */
@Data
@Builder
public class CartItemVO {
    // 商品的编号 SkuId
    private Long skuId;

    private Long spuId;
    // 商品的图片
    private String image;
    // 商品的标题
    private String title;
    // 是否选中
    private boolean check = true;
    // 商品的销售属性
    private List<String> skuAttr;
    // 商品的单价
    private BigDecimal price;
    // 购买的数量
    private Integer count;
    // 商品的总价
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(count));
    }
}
