package com.personal.mall.product.entity.vo;

import lombok.Data;

@Data
public class AttrResponseVO extends AttrGroupEntityVO{
    private String groupName;
    private String catelogName;
    private Long[] catelogPath;
}
