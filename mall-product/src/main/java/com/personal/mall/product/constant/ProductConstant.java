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
}
