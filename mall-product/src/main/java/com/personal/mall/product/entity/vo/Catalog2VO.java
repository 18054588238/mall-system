package com.personal.mall.product.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Catalog2VO
 * @Author liupanpan
 * @Date 2025/8/29
 * @Description 需要展示的二级分类数据vo
 */
@Data
public class Catalog2VO {

    private String catalog1Id; // 对应的一级分类编号
    private List<Catalog3VO> catalog3List;// 对应的三级分类信息
    private String id; // 二级分类编号
    private String name;

    @Data
    public static class Catalog3VO {
        private String catalog2Id;// 三级分类对应的二级分类编号
        private String id;
        private String name;
    }

}
