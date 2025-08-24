package com.personal.common.constant;

import lombok.Data;
import lombok.Getter;

public class WareConstant {
    // 采购需求状态
    public enum PurchaseDetailStatus {
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");
        @Getter
        private int code;
        @Getter
        private String msg;
        PurchaseDetailStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
    // 采购单状态
    public enum PurchaseStatus {
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        @Getter
        private int code;
        @Getter
        private String msg;

        PurchaseStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
