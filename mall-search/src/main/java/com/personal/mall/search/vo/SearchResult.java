package com.personal.mall.search.vo;

import com.personal.common.dto.SkuESModel;
import lombok.Data;

import java.util.List;

/*检索后的响应信息*/
@Data
public class SearchResult {
    private List<SkuESModel> products;// ES中的商品信息
    // 分页信息
    private Integer pageNum; // 当前页
    private Long total; // 总记录数
    private Integer totalPages; // 总页数
    private List<Integer> navs; //需要显示的分页的页码

    // 当前查询的所有商品所涉及到的所有
    // 品牌信息
    private List<BrandVO> brands;
    // 属性信息
    private List<AttrVO> attrs;
    // 类别信息
    private List<CatalogVO> catalogs;

    @Data
    public static class BrandVO {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class AttrVO {
        private Long attrId;
        private String attrName;
        private List<String> attrValue; // 属性值
    }
    @Data
    public static class CatalogVO {
        private Long catalogId;
        private String catalogName;
    }
}