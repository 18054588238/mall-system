package com.personal.mall.product.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName SeckillSkuVO
 * @Author liupanpan
 * @Date 2025/10/10
 * @Description
 */
@Data
public class SeckillSkuVO {
    private Date startTime;

    private Date endTime;

    // 随机码
    private String randomCode;

    private SeckillSkuRelationVO seckillSkuRelationVO;

    @Data
    static class SeckillSkuRelationVO {

        private Long id;
        /**
         * 活动id
         */
        private Long promotionId;
        /**
         * 活动场次id
         */
        private Long promotionSessionId;
        /**
         * 商品id
         */
        private Long skuId;
        /**
         * 秒杀价格
         */
        private BigDecimal seckillPrice;
        /**
         * 秒杀总量
         */
        private BigDecimal seckillCount;
        /**
         * 每人限购数量
         */
        private BigDecimal seckillLimit;
        /**
         * 排序
         */
        private Integer seckillSort;
    }
}
