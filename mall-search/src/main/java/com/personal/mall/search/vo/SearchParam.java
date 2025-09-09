package com.personal.mall.search.vo;

import lombok.Data;

import java.util.List;

/*封装检索条件*/
@Data
public class SearchParam {
    private String keyword; // 全文检索关键字
    private Long catalog3Id; // 根据分类查询的编号
    /**
     * sort=salaCount_asc/desc
     * sort=skuPrice_asc/desc
     * sort=hotScore_asc/desc
     */
    private String sort; // 排序条件
    // 查询的筛选条件  hasStock=0/1;
    private Integer hasStock = 1;// 是否只显示有货的商品
    // brandId=1&brandId=2
    private List<Long> brandId; // 查询的品牌id
    // skuPrice=200_300
    // skuPrice=_300
    // skuPrice=200_
    private String skuPrice; // 商品的价格区间
    // 不同的属性  attrs:1_苹果:6.5寸
    private List<String> attrs; // 商品的属性
    private Integer pageNum = 1; // 页码
}
