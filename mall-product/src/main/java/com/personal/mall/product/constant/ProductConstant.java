package com.personal.mall.product.constant;

import lombok.Getter;

public class ProductConstant {
    @Getter
    public enum AttrType{
        BASE_ATTR_TYPE(1,"基本属性"),
        SALE_ATTR_TYPE(0,"销售属性");
        private int code;
        private String msg;
        AttrType(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Getter
    public enum PublishStatus{
        SPU_NEW(0,"新建"),
        SPU_UP(1,"上架"),
        SPU_DOWN(2,"下架");
        private int code;
        private String msg;
        PublishStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
