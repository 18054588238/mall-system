package com.personal.mall.seckill.dto;

import com.personal.mall.seckill.vo.SeckillSkuRelationVO;
import com.personal.mall.seckill.vo.SeckillSkuSessionVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SeckillSkuRedisDTO {

    private Date startTime;

    private Date endTime;

    // 随机码
    private String randomCode;

    private SeckillSkuRelationVO seckillSkuRelationVO;

    private SkuInfoVO skuInfo;

    @Data
    public static class SkuInfoVO {
        private Long skuId;
        /**
         * spuId
         */
        private Long spuId;
        /**
         * sku名称
         */
        private String skuName;
        /**
         * sku介绍描述
         */
        private String skuDesc;
        /**
         * 所属分类id
         */
        private Long catalogId;
        /**
         * 品牌id
         */
        private Long brandId;
        /**
         * 默认图片
         */
        private String skuDefaultImg;
        /**
         * 标题
         */
        private String skuTitle;
        /**
         * 副标题
         */
        private String skuSubtitle;
        /**
         * 价格
         */
        private BigDecimal price;
        /**
         * 销量
         */
        private Long saleCount;
    }
}
